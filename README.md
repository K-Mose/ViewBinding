# ViewBinding


## ViewBinding vs DataBinding
<p><b> There is nothing viewbinding can do that databnidng cannot do </b></p>
ViewBinding은 단순히 View와 Binding하는 반변에 DataBinding은 View에게 표현식으로 data까지 제공할 수 있습니다. <br>
하지만 ViewBinding을 사용함으로 아래와 같은 이점이 있습니다. <br>

***view binding의 이점*** <br>
- easy to use : XML에 `<layout>` 태그가 필요 없고 추가적인 표현식을 쓸 필요가 없음
- faster compile time : 처리 과정에서 어노테이션을 필요로하지 않아 컴파일 시간에 더 빠름
  
View에서 id로 객체를 직접 접근하기 때문에 findViewById 보다 Compile time에 더 안전하며 DataBinding보다 속도도 빠릅니다. 
DataBinding을 단순히 View에 접근하기 위해서 사용했다면 ViewBinding으로 변경하는 것이 훨씬 효율적입니다. 
  
### Simple Example
우선 ViewBinding을 적용하기 위해선 App level의 `build.gradle`파일에 viewBinding을 적용해야 합니다.
```
    buildFeatures {
        viewBinding true
    }
```
방법은 DataBinding을 적용하는 것과 동일합니다. 

그리고 기존 DataBinding에 필요했던 xml 파일 안에 `<layout>`을 제거합니다. 
```xml
<?xml version="1.0" encoding="utf-8"?>
<layout
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    xmlns:android="http://schemas.android.com/apk/res/android">
    <androidx.constraintlayout.widget.ConstraintLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        tools:context=".MainActivity">
        ……
```
to
```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".MainActivity">
    ……
```

마지막으로 Activity에서 Layout의 inflate 방식을 변경합니다. DataBiding을 위해 사용했던 Activity…Binding 객체는 변경하지 않고 그대로 사용합니다. 
```kotlin 
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setContentView(R.layout.activity_main)
```
to
```kotlin 
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
```

