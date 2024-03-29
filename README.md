# Non-uniform Zooming with Multi-touch Technology

## Introduction
Non-uniform zooming with multi-touch technology is a project developed to address the challenge of visualizing information on small screens, such as those on mobile devices. This Android application allows users to zoom in on image parts non-uniformly using natural finger gestures, providing an efficient and entertaining experience.

## Features
- Multiple zooming areas on a single screen
- Interactive control over the zooming size
- Onion zooming for hierarchical zoom levels
- Quick response time

## System Requirements
- Android device with version 2.2 or higher

## Installation
1. Download the APK file from the repository.
2. Install the APK on your Android device.

## Usage
- To zoom in: Use two fingers to spread apart on the screen area you wish to zoom in.
- To zoom out: Pinch two fingers together on the zoomed area.
- For onion zooming: Perform the zoom in gesture on an already zoomed area.

## Technology
The application is developed using the Android multi-touch SDK and implements the Barrel Distortion Correction Algorithm with Bilinear Interpolation to achieve the non-uniform zooming effect.

## Development and Design
The project follows a User-Centric Design approach, evolving through multiple phases from initial concept to final implementation, focusing on user feedback and iterative testing.

## Known Issues
- Limited to handling two simultaneous touch points.
- Large images may cause memory out-of-bounds errors.
- Repeated quick gestures before response completion can cause errors.

## Future Work
- Improving touch event handling to support more simultaneous gestures.
- Enhancing the application to handle larger images without errors.
- Adding functionality to move zoomed areas across the screen.

## Additional Resources
For more details on the project, refer to the following documents:
- [Project Report](https://github.com/iamnhk/non-uniform-zooming/blob/main/Report.doc) - Detailed report on the Non-uniform Zooming project.
- [University of Helsinki Presentation](https://github.com/iamnhk/non-uniform-zooming/blob/main/University%20of%20Helsinki.ppt) - Presentation slides providing an overview of the project.

## Credits
- Md. Nazmul Haque Khan (Author)
- Mohsen Koolaji, Zinat Rasooli Mavini, Yina Ye (Contributors)

## References
1. Carl Gutwin & Chris Fedak, "Interacting with Big Interfaces on Small Screens: a Comparison of Fisheye Zoom and Panning Techniques."
2. [JPsEffects](http://www.jpseffects.de/JPEZoom2_EN.html)
3. Carl Gutwin & Chris Fedak, "A Comparison of Fisheye Lenses for Interactive Layout Tasks."
4. K.T. Gribbon, C.T. Johnston, and D.G. Bailey, "A Real-time FPGA Implementation of a Barrel Distortion Correction Algorithm with Bilinear Interpolation."
