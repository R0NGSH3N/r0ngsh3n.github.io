---
layout: post
title: Change display resolution via xrandr and gtf
tags: Linux Manjaro ArchLinux
categories: common
---

## list all your currently resolution:

~~~bash

╭─    ~/projects/r0ngsh3n-vim    main ?1 ············································································· ✔  3s  ─╮
╰─ xrandr                                                                                                                               ─╯
Screen 0: minimum 1 x 1, current 2560 x 1600, maximum 8192 x 8192
Virtual1 connected primary 2560x1600+0+0 (normal left inverted right x axis y axis) 0mm x 0mm
   800x600       60.00 +  60.32  
   2560x1600     59.99* 
   1920x1440     60.00  
   1856x1392     60.00  
   1792x1344     60.00  
   1920x1200     59.88  
   1600x1200     60.00  
   1680x1050     59.95  
   1400x1050     59.98  
   1280x1024     60.02  
   1440x900      59.89  
   1280x960      60.00  
   1360x768      60.02  
   1280x800      59.81  
   1152x864      75.00  
   1280x768      59.87  
   1024x768      60.00  
   640x480       59.94  
Virtual2 disconnected (normal left inverted right x axis y axis)
Virtual3 disconnected (normal left inverted right x axis y axis)
Virtual4 disconnected (normal left inverted right x axis y axis)
Virtual5 disconnected (normal left inverted right x axis y axis)
Virtual6 disconnected (normal left inverted right x axis y axis)
Virtual7 disconnected (normal left inverted right x axis y axis)
Virtual8 disconnected (normal left inverted right x axis y axis)

~~~

## if the resolution is in the list, then you can just use following command to
switch 

~~~bash
xrandr -s 1920x1440
~~~

## if not, then you need to use `gtf` tool to calucate your customized
resolution config, eg, if you want to set your resolution to `2560x1440`, then
you can run following command:

~~~bash
╭─    ~/projects/r0ngsh3n-vim    main ?1 ·················································································· 1 ✘ ─╮
╰─ gtf 2560 1440 60                                                                                                                     ─╯

  # 2560x1440 @ 60.00 Hz (GTF) hsync: 89.40 kHz; pclk: 311.83 MHz
  Modeline "2560x1440_60.00"  311.83  2560 2744 3024 3488  1440 1441 1444 1490  -HSync +Vsync

~~~

you see the `Modeline` gtf has calculate for you. That is the config you need to
add to xrandr.


## Create new mode in xrandr

~~~bash
sudo xrandr --newmode "2560x1440_60.00"  311.83  2560 2744 3024 3488  1440 1441 1444 1490  -HSync +Vsync                             ─╯
~~~

after you create new mode in xrandr, when you list the mode, it show that mode
in the bottom with unassigned

~~~bash
xrandr                                                                                                                               ─╯
Screen 0: minimum 1 x 1, current 2560 x 1600, maximum 8192 x 8192
Virtual1 connected primary 2560x1600+0+0 (normal left inverted right x axis y axis) 0mm x 0mm
   800x600       60.00 +  60.32  
   2560x1600     59.99* 
   1920x1440     60.00  
   1856x1392     60.00  
   1792x1344     60.00  
   1920x1200     59.88  
   1600x1200     60.00  
   1680x1050     59.95  
   1400x1050     59.98  
   1280x1024     60.02  
   1440x900      59.89  
   1280x960      60.00  
   1360x768      60.02  
   1280x800      59.81  
   1152x864      75.00  
   1280x768      59.87  
   1024x768      60.00  
   640x480       59.94  
Virtual2 disconnected (normal left inverted right x axis y axis)
Virtual3 disconnected (normal left inverted right x axis y axis)
Virtual4 disconnected (normal left inverted right x axis y axis)
Virtual5 disconnected (normal left inverted right x axis y axis)
Virtual6 disconnected (normal left inverted right x axis y axis)
Virtual7 disconnected (normal left inverted right x axis y axis)
Virtual8 disconnected (normal left inverted right x axis y axis)
  2560x1440_60.00 (0x559) 311.830MHz -HSync +VSync
        h: width  2560 start 2744 end 3024 total 3488 skew    0 clock  89.40KHz
        v: height 1440 start 1441 end 1444 total 1490           clock  60.00Hz
~~~

## Assign new mode to the display -- "Virtual1"

~~~bash
xrandr --addmode Virtual1 "2560x1440_60.00"
~~~

after you assign, run the xrandr you will see the `unassigned` resolution now is
listed 

~~~bash
 xrandr                                                                                                                               ─╯
Screen 0: minimum 1 x 1, current 2560 x 1600, maximum 8192 x 8192
Virtual1 connected primary 2560x1600+0+0 (normal left inverted right x axis y axis) 0mm x 0mm
   800x600       60.00 +  60.32  
   2560x1600     59.99* 
   1920x1440     60.00  
   1856x1392     60.00  
   1792x1344     60.00  
   1920x1200     59.88  
   1600x1200     60.00  
   1680x1050     59.95  
   1400x1050     59.98  
   1280x1024     60.02  
   1440x900      59.89  
   1280x960      60.00  
   1360x768      60.02  
   1280x800      59.81  
   1152x864      75.00  
   1280x768      59.87  
   1024x768      60.00  
   640x480       59.94  
   2560x1440_60.00  60.00  
Virtual2 disconnected (normal left inverted right x axis y axis)
Virtual3 disconnected (normal left inverted right x axis y axis)
Virtual4 disconnected (normal left inverted right x axis y axis)
Virtual5 disconnected (normal left inverted right x axis y axis)
Virtual6 disconnected (normal left inverted right x axis y axis)
Virtual7 disconnected (normal left inverted right x axis y axis)
Virtual8 disconnected (normal left inverted right x axis y axis)

~~~

if you see the new resolution is in the "Virtual1"'s list, then you can use `-s`
    optino to change to that new resolution.
