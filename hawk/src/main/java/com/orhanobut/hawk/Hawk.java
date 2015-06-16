package com.orhanobut.hawk;

import android.content.Context;
import android.util.Pair;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides a secure simple key-value store
 *
 * @author Orhan Obut
 */
public final class Hawk {

    //never ever change this value since it will break backward compatibility in terms of keeping previous data
    private static final String TAG = "HAWK";
    //never ever change this value since it will break backward compatibility in terms of keeping previous data
    private static final String TAG_CRYPTO = "324909sdfsd98098";

    private Encoder encoder;
    private Storage storage;
    private Encryption encryption;
    private LogLevel logLevel;
    private Logger logger;

    /**
     * Used for building an instance of Hawk
     * Builder pattern borrowed from Effective Java, Chapter 2
     * https://stackoverflow.com/questions/2169190/example-of-builder-pattern-in-java-api
     */
    public static class Builder {
        // Required parameters
        private final Context context;
        private final String password;

        // Optional parameters - initialized to default values
        private String salt;
        private Encoder encoder;
        private Storage storage;
        private Encryption encryption;
        private LogLevel logLevel = LogLevel.NONE;
        private Logger logger;

        public Builder(Context context, String password) {
            if (context == null) {
                throw new IllegalArgumentException("context must not be null");
            }
            if (password == null) {
                throw new IllegalArgumentException("password must not be null");
            }
            this.context = context;
            this.password = password;
        }

        public Builder salt(String salt) {
            this.salt = salt;
            return this;
        }

        public Builder encoder(Encoder encoder) {
            this.encoder = encoder;
            return this;
        }

        public Builder storage(Storage storage) {
            this.storage = storage;
            return this;
        }

        public Builder encryption(Encryption encryption) {
            this.encryption = encryption;
            return this;
        }

        public Builder logLevel(LogLevel logLevel) {
            this.logLevel = logLevel;
            return this;
        }

        public Builder logger(Logger logger) {
            this.logger = logger;
            return this;
        }

        public Hawk build() {
            return new Hawk(this);
        }
    }

    private Hawk(Builder builder) {
        Context appContext = builder.context.getApplicationContext();
        logger = (builder.logger != null) ? builder.logger : new Logger(logLevel);
        encryption = (builder.encryption != null) ? builder.encryption :
                new AesEncryption(
                        logger,
                        new SharedPreferencesStorage(appContext, TAG_CRYPTO),
                        builder.password,
                        builder.salt
                );
        encoder = (builder.encoder != null) ? builder.encoder :
                new HawkEncoder(logger, encryption, new GsonParser(new Gson()));
        storage = (builder.storage != null) ? builder.storage : new SharedPreferencesStorage(appContext, TAG);
        logLevel = builder.logLevel;
    }

    /**
     * Saves every type of Objects. List, List<T>, primitives
     *
     * @param encryptionKey Separate key to encrypt value
     * @param key   is used to save the data
     * @param value is the data that is gonna be saved. Value can be object, list type, primitives
     * @return true if put is successful
     */
    public <T> boolean putWithEncryptionKey(String encryptionKey, String key, T value) {
        if (encryptionKey == null) {
            throw new NullPointerException("encryptionKey cannot be null");
        }

        if (key == null) {
            throw new NullPointerException("Key cannot be null");
        }

        String encodedText = encode(encryptionKey, value);
        //if any exception occurs during encoding, encodedText will be null and thus operation is unsuccessful
        if (encodedText == null) {
            return false;
        }

        return storage.put(key, encodedText);
    }

    /**
     * Saves every type of Objects. List, List<T>, primitives
     *
     * @param key   is used to save the data
     * @param value is the data that is gonna be saved. Value can be object, list type, primitives
     * @return true if put is successful
     */
    public <T> boolean put(String key, T value) {
        if (key == null) {
            throw new NullPointerException("Key cannot be null");
        }

        String encodedText = encode(value);
        //if any exception occurs during encoding, encodedText will be null and thus operation is unsuccessful
        if (encodedText == null) {
            return false;
        }
        return storage.put(key, encodedText);
    }

    /**
     * Saves the list of objects to the storage
     *
     * @param key  is used to save the data
     * @param list is the data that will be saved
     * @return true if put is successful
     */
    public <T> boolean put(String key, List<T> list) {
        if (key == null) {
            throw new NullPointerException("Key cannot be null");
        }

        String encodedText = encode(list);
        //if any exception occurs during encoding, encodedText will be null and thus operation is unsuccessful
        if (encodedText == null) {
            return false;
        }
        return storage.put(key, encodedText);
    }

    /**
     * Encodes the given value as full text (cipher + data info)
     *
     * @param encryptionKey is the key used to encrypt value
     * @param value is the given value to encode
     * @return full text as string
     */
    private <T> String encode(String encryptionKey, T value) {
        if (value == null) {
            throw new NullPointerException("Value cannot be null");
        }
        String cipherText = encoder.encode(encryptionKey, value);
        if (cipherText == null) {
            return null;
        }
        return DataUtil.addTypeAsObject(cipherText, value.getClass());
    }

    /**
     * Encodes the given value as full text (cipher + data info)
     *
     * @param value is the given value to encode
     * @return full text as string
     */
    private <T> String encode(T value) {
        if (value == null) {
            throw new NullPointerException("Value cannot be null");
        }
        String cipherText = encoder.encode(value);
        if (cipherText == null) {
            return null;
        }
        return DataUtil.addTypeAsObject(cipherText, value.getClass());
    }

    /**
     * Encodes the given list as full text (cipher + data info)
     *
     * @param list is the given list to encode
     * @return full text as string
     */
    private <T> String encode(List<T> list) {
        if (list == null) {
            throw new NullPointerException("List<T> cannot be null");
        }
        if (list.size() == 0) {
            throw new IllegalStateException("List<T> cannot be empty");
        }
        String cipherText = encoder.encode(list);
        if (cipherText == null) {
            return null;
        }
        Class clazz = list.get(0).getClass();
        return DataUtil.addTypeAsList(cipherText, clazz);
    }

    /**
     * @param key is used to get the saved data
     * @return the saved object
     */
    public <T> T getWithEncryptionKey(String encryptionKey, String key) {
        if (key == null) {
            throw new NullPointerException("Key cannot be null");
        }
        String fullText = storage.get(key);
        try {
            return encoder.decode(encryptionKey, fullText);
        } catch (Exception e) {
            logger.d(e.getMessage());
        }
        return null;
    }

    /**
     * @param key is used to get the saved data
     * @return the saved object
     */
    public <T> T get(String key) {
        if (key == null) {
            throw new NullPointerException("Key cannot be null");
        }
        String fullText = storage.get(key);
        try {
            return encoder.decode(fullText);
        } catch (Exception e) {
            logger.d(e.getMessage());
        }
        return null;
    }

    /**
     * Gets the saved data, if it is null, default value will be returned
     *
     * @param key          is used to get the saved data
     * @param defaultValue will be return if the response is null
     * @return the saved object
     */
    public <T> T get(String key, T defaultValue) {
        T t = get(key);
        if (t == null) {
            return defaultValue;
        }
        return t;
    }

    /**
     * Enables chaining of multiple put invocations.
     *
     * @return a simple chaining object
     */
    public Chain chain() {
        return new Chain();
    }

    /**
     * Enables chaining of multiple put invocations.
     *
     * @param capacity the amount of put invocations you're about to do
     * @return a simple chaining object
     */
    public Chain chain(int capacity) {
        return new Chain(capacity);
    }

    /**
     * Size of the saved data. Each key will be counted as 1
     *
     * @return the size
     */
    public int count() {
        return storage.count();
    }

    /**
     * Clears the storage, note that crypto data won't be deleted such as salt key etc.
     * Use resetCrypto in order to clear crypto information
     *
     * @return true if clear is successful
     */
    public boolean clear() {
        return storage.clear();
    }

    /**
     * Removes the given key/value from the storage
     *
     * @param key is used for removing related data from storage
     * @return true if remove is successful
     */
    public boolean remove(String key) {
        return storage.remove(key);
    }

    /**
     * Removes values associated with the given keys from the storage
     *
     * @param keys are used for removing related data from storage
     * @return true if all removals are successful
     */
    public boolean remove(String... keys) {
        return storage.remove(keys);
    }

    /**
     * Checks the given key whether it exists or not
     *
     * @param key is the key to check
     * @return true if it exists in the storage
     */
    public boolean contains(String key) {
        return storage.contains(key);
    }

    /**
     * Clears all saved data that is used for the crypto
     *
     * @return true if reset is successful
     */
    public boolean resetCrypto() {
        return encryption.reset();
    }

    public LogLevel getLogLevel() {
        return logLevel;
    }

    /**
     * Provides the ability to chain put invocations:
     * <code>hawk.chain().put("foo", 0).put("bar", false).commit()</code>
     * <p/>
     * <code>commit()</code> writes the chain values to persistent storage. Omitting it will
     * result in all chained data being lost.
     */
    public final class Chain {

        private final List<Pair<String, ?>> items;

        public Chain() {
            this(10);
        }

        public Chain(int capacity) {
            items = new ArrayList<>(capacity);
        }

        /**
         * Saves every type of Objects. List, List<T>, primitives
         *
         * @param key   is used to save the data
         * @param value is the data that is gonna be saved. Value can be object, list type, primitives
         */
        public <T> Chain put(String key, T value) {
            if (key == null) {
                throw new NullPointerException("Key cannot be null");
            }
            String encodedText = encode(value);
            if (encodedText == null) {
                logger.d("Key : " + key + " is not added, encryption failed");
                return this;
            }
            items.add(new Pair<>(key, encodedText));
            return this;
        }

        /**
         * Saves the list of objects to the storage
         *
         * @param key  is used to save the data
         * @param list is the data that will be saved
         */
        public <T> Chain put(String key, List<T> list) {
            if (key == null) {
                throw new NullPointerException("Key cannot be null");
            }
            String encodedText = encode(list);
            if (encodedText == null) {
                logger.d("Key : " + key + " is not added, encryption failed");
                return this;
            }
            items.add(new Pair<>(key, encodedText));
            return this;
        }

        /**
         * Commits the chained values to storage.
         *
         * @return true if successfully saved, false otherwise.
         */
        public boolean commit() {
            return storage.put(items);
        }

    }

}
