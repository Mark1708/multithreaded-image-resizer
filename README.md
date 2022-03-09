# multithreaded-image-resizer
![Java](https://img.shields.io/badge/-Java-0a0a0a?style=for-the-badge&logo=Java)
<br/>

>This is a simple project made for educational purposes to practice multithreaded programming.

## Table of contents
* [General info](#general-info)
* [Technologies](#technologies)
* [Features](#features)
* [Status](#status)
* [Inspiration](#inspiration)
* [Contact](#contact)

## General info
This console application allows you to increase and decrease the size of the original image(s)

Resizing algorithms:
* resize by nearest neighbor algorithm
* resize by imgscalr
* resize by Graphics2D with RenderingHints


To run the application, you must run the riser.jar which I added to the assets folder or run the `mvn package` command (the jar will be in the target folder)

## Technologies
* Java - version 11

## Code Examples
### About using
```
➜ java -jar resizer.jar -h
    Usage: <main class> [options]
      Options:
        --dst, -d
          Path to destination directory 
          Default: (by default image(s) will be saved in src directory)
        --mode, -m
          Mode of resizing image:
              1 - resize by nearest neighbor algorithm; 
              2 - resize by imgscalr; 
              3 - resize by Graphics2D with RenderingHints
          Default: 3
        --size, -S
          New image size (by default the image will be resized to size 300px)
          Default: 300
      * --src, -s
          Path to source image/directory
```
### Resize all images in src folder to 400px and write to dst folder
```
➜ java -jar resizer.jar -s /path/src -d /path/dst -S 400 -m 3
    Duration: 19677
    Count of objects: 501
```
### Resize images to 1920px
```
➜ java -jar resizer.jar -s /path/image.jpg -S 1920 -m 2
    Duration: 6273
    Count of objects: 1
```
#### Result
Before | After
------ | ------
![before](https://github.com/Mark1708/multithreaded-image-resizer/raw/master/assets/image.jpg?raw=true)   | ![after](https://github.com/Mark1708/multithreaded-image-resizer/raw/master/assets/image_resized.jpg?raw=true)
3648  x  5472 | 1920  x  2880
## Features
* Multithreading
* Nearest Neighbor Algorithm
* imgscalr
* Graphics2D
* JCommander

## Status
Project is: _finished_

## Inspiration
The project was created for educational purposes

## Contact
Created by [Gurianov Mark](https://mark1708.github.io/) - feel free to contact me!
#### +7(962)024-50-04 | mark1708.work@gmail.com | [github](http://github.com/Mark1708)

![Readme Card](https://github-readme-stats.vercel.app/api/pin/?username=mark1708&repo=multithreaded-image-resizer&theme=chartreuse-dark&show_icons=true)
