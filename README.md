[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-Hawk-brightgreen.svg?style=flat)](https://android-arsenal.com/details/1/1568)      [![API](https://img.shields.io/badge/API-8%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=8)   [![Join the chat at https://gitter.im/orhanobut/hawk](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/orhanobut/hawk?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)  [![](https://img.shields.io/badge/AndroidWeekly-%23141-blue.svg)](http://androidweekly.net/issues/issue-141) 

# Hawk
Secure, simple key-value storage for android

<img src='https://github.com/orhanobut/hawk/blob/master/images/hawk-logo.png' width='128' height='128'/>

Hawk uses:
- AES for the crypto
- SharedPreferences for the storage
- Gson for parsing

Hawk provides:
- Secure data persistence
- Save any type
- Save list of any type

### Add dependency
```groovy
repositories {
    maven { url "https://oss.sonatype.org/content/repositories/snapshots/"}
}
dependencies {
    compile 'com.orhanobut:hawk:1.4-SNAPSHOT'
}
```

#### Create a hawk instance
```java
Hawk hawk = new Hawk(context, PASSWORD, SALT);
```

#### Save
```java
hawk.put(key, T);
```
or
```java
hawk.put(key, List<T>);
```

#### Get
```java
T result = hawk.get(key);
```
or with default value

```java
T result = hawk.get(key, T);
```

#### Remove
```java
hawk.remove(key);
```

#### Contains
```java
boolean contains = hawk.contains(key);
```

#### Set the log output (optional)
```java
hawk.init(context, PASSWORD, LogLevel.FULL); // as default it is NONE
```

##### More samples for save

```java
hawk.put("key", "something"); // Save string
hawk.put("key", true); // save boolean
hawk.put("key", new Foo()); // save an object
hawk.put("key", List<String>); // save list
hawk.put("key", List<Foo>); // save list of any type
hawk.put("key", 1234); // save numbers
```

##### More samples for get

```java
String value = hawk.get(key);
int value = hawk.get(key);
Foo value = hawk.get(key);
boolean value = hawk.get(key);
List<String> value = hawk.get(key);
List<Foo> value = hawk.get(key);
```
or with the defaults
```java
String value = hawk.get(key, "");
int value = hawk.get(key, 0);
Foo value = hawk.get(key, new Foo());
boolean value = hawk.get(key, false);
List<String> value = hawk.get(key, Collections.emptyList());
List<Foo> value = hawk.get(key, new ArrayList<Foo>);
```

##### Benchmark result (ms)
Done with Nexus 4, Android L. Note that this is not certain values, I just made a few runs and show it to give you an idea.

<img src='https://github.com/orhanobut/hawk/blob/master/images/benchmark.png'/>

##### How Hawk works

<img src='https://github.com/orhanobut/hawk/blob/master/images/flow-chart.png'/>

##### Notes
- Password should be provided by the user, we try to find better solution for this.
- Hawk.init() takes around 200-500ms depends on the phone.
- Salt key is stored plain text in the storage currently. We are checking to find a better solution for this. Any contribution about this will be great help as well.

##### Credits
I use the following implementation for the crypto and I believe it should get more attention. Thanks for this great hard work. https://github.com/tozny/java-aes-crypto and a great article about it : http://tozny.com/blog/encrypting-strings-in-android-lets-make-better-mistakes/

### License
<pre>
Copyright 2015 Orhan Obut

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
</pre>
