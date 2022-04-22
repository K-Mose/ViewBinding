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

## Android Service Example With ViewBinding 
안드로이드에서 Service는 background에서 장시간 작업을 수행하기 위한 <a href="https://developer.android.com/guide/components/fundamentals#Components">Appllication Component</a> 입니다.
Service는 UI를 제공하지 않으며 다른 컴포턴트를 통해서 실행될 수 있으며 앱이 종료되어도 background에서 작업될 수 있습니다. 

ViewBinding이 수행되면 Service가 실행되는 예를 추가해보겠습니다. 
<details>
  <summary>Setup</summary>
  <details>
    <summary><b>ServiceActivity.kt</b></summary>
    
```kotlin
class ServiceActivity : AppCompatActivity() {
    private lateinit var binding: ActivityServiceBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityServiceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // background Service
        binding.btnStart.setOnClickListener {

        }

        binding.btnStop.setOnClickListener {

        }
    }
}  
```
  </details>

  <details>
    <summary><b>activity_service.xml</b></summary>
  
```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".ServiceActivity">

    <Button
        android:id="@+id/btn_start"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="start"
        android:textSize="30dp"
        app:layout_constraintVertical_bias="0.3"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintLeft_toLeftOf="parent"
        app:layout_constraintRight_toRightOf="parent"
        app:layout_constraintBottom_toBottomOf="parent"
        />
    <Button
        android:id="@+id/btn_stop"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="stop"
        android:textSize="30dp"
        app:layout_constraintVertical_bias="0.2"
        app:layout_constraintTop_toBottomOf="@id/btn_start"
        app:layout_constraintLeft_toLeftOf="parent"
        app:layout_constraintRight_toRightOf="parent"
        app:layout_constraintBottom_toBottomOf="parent"
        />

</androidx.constraintlayout.widget.ConstraintLayout>
```
  </details>
  <details>
    <summary><b>MyBackgroundService.kt</b></summary>
    
```kotlin 
class MyBackgroundService : Service() {
    init {
        Log.i("MYTAG", "Service has been created")
    }
    
    override fun onBind(p0: Intent?): IBinder? = null
}
```
    
  </details>

</details>

우선 생성한 `MyBackgroundService`클래스에 Service가 `startService()`함수로 시작되면 실행되는 `onStartCommand()` 함수를 추가합니다. 
그리고 Service가 종료될 때 실행되는 `onDestroy()`함수를 추가합니다. 
```kotlin
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.i("MYTAG", "Service Started")
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("MYTAG", "Destroying ...")
    }
```
`onStartCommand()`의 return 플래그들은 현재 시작된 서비스가 종료됨에 따라 나타내는 값 입니다. 

그리고 `Activity`로 돌아와 Service를 Intent에 등록한 후 각각 버튼 리스너에 Service 실행과 종료함수를 추가합니다. 
```kotlin
        val serviceIntent = Intent(this, MyBackgroundService::class.java)
        
        binding.btnStart.setOnClickListener {
            Log.i("MYTAG","Starting service")
            startService(serviceIntent)
        }

        binding.btnStop.setOnClickListener {
            Log.i("MYTAG","Stopping service")
            stopService(serviceIntent)
        }
```

마지막으로 `AndroidManifest`에 서비스를 등록합니다. 
```xml
    <application>
        ……
        <service android:name=".MyBackgroundService"/>
    </application>
```
<details>
  <summary><b>Logcat</b></summary>
  
  ![image](https://user-images.githubusercontent.com/55622345/164622234-42b3793a-aa09-4eaa-9d37-e0c268f5681e.png) <br>
  서비스가 실행되고 종료됨을 확인 할 수 있습니다.
</details>
