/*
-----------------------------------------------------
PlacentaAnalyzer v0.2

Author:
Deborah + OpenAI

Purpose:
Generate compartment ROIs from Weka classified images.

Current Version:
0.2

Class IDs

0 = Background
1 = Labyrinth
2 = Junctional Zone
3 = Excluded
-----------------------------------------------------
*/

import ij.IJ
import ij.ImagePlus

import ij.gui.Roi

import ij.plugin.filter.ThresholdToSelection
import ij.plugin.frame.RoiManager

import ij.process.ByteProcessor
import ij.process.ImageProcessor

//----------------------------------------------------
// CLASS IDS
//----------------------------------------------------
final int BACKGROUND = 0
final int LABYRINTH = 1
final int JUNCTION = 2
final int EXCLUDED = 3

//----------------------------------------------------
// MAIN
//----------------------------------------------------

def main() {

    ImagePlus classified = IJ.getImage()

    if (classified == null) {
        IJ.error("No classified image open!")
        return
    }

    println("Image loaded successfully.")

    ImageProcessor ip = classified.getProcessor()

    int width = ip.getWidth()
    int height = ip.getHeight()

    println("Image width  = " + width)
    println("Image height = " + height)
// Test reading one pixel
int pixelValue = ip.getPixel(500, 300)

println("Pixel (500,300) = " + pixelValue)

Roi labyrinthROI = processCompartment(classified, 1)
addROIToManager(labyrinthROI)
applyROI(classified, labyrinthROI)

println("ROI created: " + (labyrinthROI != null))
println("Pixel (0,0) = " + ip.getPixel(0,0))
println("Pixel (100,100) = " + ip.getPixel(100,100))
println("Pixel (1000,500) = " + ip.getPixel(1000,500))
}

main()//----------------------------------------------------
// CREATE BINARY MASK
//----------------------------------------------------

ImagePlus createBinaryMask(ImagePlus classified, int classID){

    ImageProcessor input = classified.getProcessor()

    int width = input.getWidth()
    int height = input.getHeight()

    ByteProcessor mask = new ByteProcessor(width, height)

    for (int y = 0; y < height; y++) {

        for (int x = 0; x < width; x++) {

            int pixel = input.getPixel(x, y)

            if (pixel == classID) {
                mask.set(x, y, 255)
            } else {
                mask.set(x, y, 0)
            }

        }

    }

return new ImagePlus("Class " + classID + " Mask", mask)
}  
//----------------------------------------------------
// CREATE ROI
//----------------------------------------------------

Roi createROI(ImagePlus binaryMask){

    ImageProcessor ip = binaryMask.getProcessor()

    ip.setThreshold(255, 255, ImageProcessor.NO_LUT_UPDATE)

    ThresholdToSelection converter = new ThresholdToSelection()

    converter.setup("", binaryMask)

    converter.run(ip)

    return binaryMask.getRoi()

}
//----------------------------------------------------
// ADD ROI TO ROI MANAGER
//----------------------------------------------------

void addROIToManager(Roi roi){

    RoiManager rm = RoiManager.getRoiManager()

    rm.addRoi(roi)

}
//----------------------------------------------------
// APPLY ROI
//----------------------------------------------------

void applyROI(ImagePlus image, Roi roi){

    image.setRoi(roi)

    image.updateAndDraw()

}
//----------------------------------------------------
// PROCESS COMPARTMENT
//----------------------------------------------------

Roi processCompartment(ImagePlus classified, int classID){

    ImagePlus mask = createBinaryMask(classified, classID)
  mask.show()
    Roi roi = createROI(mask)

    return roi

}