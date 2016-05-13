# conciseclock
![](https://img.shields.io/badge/gradle-v0.1-green.svg?style=flat-square)

This is custom can set start circle seekbar.

![](https://github.com/DuanTzXavier/conciseclock/blob/master/gif/1.gif)
![](https://github.com/DuanTzXavier/conciseclock/blob/master/gif/2.gif)

## Demo

[Demo](https://github.com/DuanTzXavier/conciseclock/blob/master/app/src/main/java/com/tomduan/conciseclock/UsageActivity.java)

## Demand

### Style
* Number
* Clock(defualt)

### Function(defualt)

#### boolean
1. `isScale(ture)`
2. `isNumberScale(ture)`
3. `isSetStart(false)`

#### color
1. `setCircleColor(int color)`
2. `setPointerColor(int color)`
3. `setInvaildColor(int color)`
4. `setSelectColor(int color)`
5. `setCenterTextColor(int color)`

#### size
1. `setCenterTextSize(int size)`
2. `setCircleWidth(int width)`
3. `setRangeWidth(int width)`
4. `setScaleTextSize(int size)`

#### other
1. `setScaleUnit()`
2. `setStyle(Style)`
3. `installItems(List<Itme> items)`

## Usage

### Gradle

You can add gradle dependency with command :

```
compile 'com.tomduan.conciseclock:library:0.1.3'
```

### FastDemo

1. add this in your xml:
```
            <com.tomduan.library.CircleSeekBar
                android:id="@+id/circle_seek_bar"
                android:layout_width="match_parent"
                android:layout_height="200000dp"
                android:layout_below="@+id/guide"
                android:padding="8dp"/>
```
2. add this in your code:
```
        mSeekBar.setCircleColor(Color.BLUE);
        mSeekBar.setPointerColor(Color.YELLOW);
        mSeekBar.setInvaildColor(Color.GREEN);
        mSeekBar.setSelectColor(Color.RED);
        mSeekBar.setTextColor(Color.BLACK);
        mSeekBar.setTextSize(50);
        mSeekBar.setCircleWidth(32);
        mSeekBar.setRangeWidth(18);
        mSeekBar.setStyle(CircleSeekBar.CLOCK);
        mSeekBar.build();
```
