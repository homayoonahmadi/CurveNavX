
# CurveNavX

Curved bottom navigation component for Android. It's powerful and flexible.

<!--- ![CurveNavX Screenshot](screenshot.png) --->

## Introduction

CurveNavX is an innovative Android library designed to enhance navigation experiences in Android applications. Offering a unique curved navigation bar, this library integrates seamlessly with Android apps, providing both functionality and aesthetic appeal.

## Features

- **Curved Navigation Bar**: Adds a visually appealing curved navigation bar to your app.
- **Customizable Elements**: Allows customization of the navigation bar's colors, icons, and labels.
- **Ease of Integration**: Designed for simple and straightforward integration with any Android project.
- **Responsive Design**: Optimized for various screen sizes and orientations.
- **High Performance**: Ensures smooth navigation without impacting app performance.

## Installation

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
    app:animationDuration="200"
    app:backgroundBottomColor="@color/cyan_700"
    >
    <!-- Add NavigationCell items here -->
</ir.programmerplus.curvenavx.BottomNavigation>
```

### In Code:

```kotlin
binding.bottomNavigation.setOnShowListener(item -> {
    binding.bottomNavigation.clearCountDelayed(item.getId(), 200);
});
```

## API Reference

Here's a table of the main methods available in CurveNavX:

| Function | Description |
| -------- | ----------- |
| `show(int itemId)` | Shows the specified item with an optional animation. |
| `setBadgeCount(int itemId, String count)` | Sets the badge count for the specified item. |
| `getBadgeCount(int itemId)` | Gets the badge count for the specified item. |
| `clearCount(int itemId)` | Clears the badge count for the specified item. |
| `clearCountDelayed(int itemId, long delayMillis)` | Clears the badge count for the specified item after a delay. |
| `clearBadgeCounts()` | Clears badge counts for all items. |

## Contribution

Contributions to CurveNavX are always welcome. Whether it's feature requests, bug reports, or pull requests, your involvement is highly appreciated.
## License

CurveNavX is released under the MIT License.

## Contact

For any queries or suggestions, feel free to reach out to the maintainer at homayoon.ahmadi8@gmail.com.
