
# CurveNavX

Curved bottom navigation component for Android. It's powerful and flexible.

<img src="https://github.com/user-attachments/assets/7e7247eb-5f05-4197-83ed-976c732a014d" width="60%"/>


## Introduction

CurveNavX is an innovative Android library designed to enhance navigation experiences in Android applications. Offering a unique curved navigation bar, this library integrates seamlessly with Android apps, providing both functionality and aesthetic appeal.


https://github.com/user-attachments/assets/c086ff11-8703-4c3b-9b2e-24b56971027b

## Features

- **Curved Navigation Bar**: Adds a visually appealing curved navigation bar to your app.
- **Customizable Elements**: Allows customization of the navigation bar's colors, icons, and labels.
- **Ease of Integration**: Designed for simple and straightforward integration with any Android project.
- **Responsive Design**: Optimized for various screen sizes and orientations.
- **High Performance**: Ensures smooth navigation without impacting app performance.

## Add to your project

### Gradle

```groovy
// Add this to your app module's build.gradle file
implementation 'ir.programmerplus.curvenavx:curvenavx:1.2.0'
```

### Maven

```xml
<!-- Add this to your dependencies -->
<dependency>
    <groupId>ir.programmerplus.curvenavx</groupId>
    <artifactId>curvenavx</artifactId>
    <version>1.2.0</version>
</dependency>
```

## Usage

### In XML:

```xml
<ir.programmerplus.curvenavx.BottomNavigation
        android:id="@+id/bottom_navigation"
        android:layout_width="match_parent"
        android:layout_height="91dp"
        android:theme="@style/BottomNavigation"
        app:animationDuration="200"
        app:backgroundBottomColor="@color/cyan_700"
        app:circleColor="@color/cyan_700"
        app:countBackgroundColor="@color/orange_800"
        app:countTextColor="@color/white"
        app:defaultIconColor="@color/gray_350"
        app:direction="rtl"
        app:hasAnimation="true"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:selectedIconColor="@color/white"
        app:shadowColor="@color/navigation_shadow">

        <!-- Add NavigationCell items here -->

        <ir.programmerplus.curvenavx.NavigationCell
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            app:icon="@drawable/avd_home"
            app:padding="3dp"
            app:selected="true"
            app:title="@string/home_page" />

        <ir.programmerplus.curvenavx.NavigationCell
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            app:badgeCount="5"
            app:icon="@drawable/avd_notification"
            app:padding="3dp"
            app:title="@string/notifications" />

        <ir.programmerplus.curvenavx.NavigationCell
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            app:badgeCount="19"
            app:icon="@drawable/avd_settings"
            app:padding="3dp"
            app:title="@string/settings" />

        <ir.programmerplus.curvenavx.NavigationCell
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            app:badgeCount="1"
            app:icon="@drawable/avd_profile"
            app:padding="3dp"
            app:title="@string/profile" />
    </ir.programmerplus.curvenavx.BottomNavigation>
```

### In Code:

```kotlin
binding.bottomNavigation.setOnShowListener { item ->
    binding.bottomNavigation.clearCountDelayed(item.id, 200)
}
```

## API Reference

Here's a table of the main methods available in CurveNavX:

| Function                                          | Description                                                  |
|---------------------------------------------------|--------------------------------------------------------------|
| `show(int itemId)`                                | Shows the specified item with an optional animation.         |
| `setBadgeCount(int itemId, String count)`         | Sets the badge count for the specified item.                 |
| `getBadgeCount(int itemId)`                       | Gets the badge count for the specified item.                 |
| `clearCount(int itemId)`                          | Clears the badge count for the specified item.               |
| `clearCountDelayed(int itemId, long delayMillis)` | Clears the badge count for the specified item after a delay. |
| `clearBadgeCounts()`                              | Clears badge counts for all items.                           |


## License

CurveNavX is released under the MIT License.
