package com.project.smartvotingsystem.FaceRecognition.imageutil;

import org.bytedeco.javacpp.opencv_core.IplImage;


public interface FaceRecognition {
    public void execute(IplImage face);
}
