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

import ij.process.ImageStatistics

import ij.plugin.Duplicator
import ij.process.BinaryProcessor

//----------------------------------------------------
// CLASS IDS
//----------------------------------------------------
final int BACKGROUND = 0
final int LABYRINTH = 1
final int JUNCTION = 2
final int EXCLUDED = 3
final int MIN_PARTICLE_AREA = 20

//----------------------------------------------------
// MAIN
//----------------------------------------------------

def main() {

ImagePlus classified = IJ.openImage()

if (classified == null){
    IJ.error("No classified image selected!")
    return
}

classified.show()

ImagePlus sirius = IJ.openImage()

if (sirius == null){
    IJ.error("No Sirius Red image selected!")
    return
}

sirius.show()
   println("Classified: " +
            classified.getWidth() + " x " +
            classified.getHeight())

    println("Sirius: " +
            sirius.getWidth() + " x " +
            sirius.getHeight())         
// Verify that both images have identical dimensions

if (classified.getWidth() != sirius.getWidth() ||
    classified.getHeight() != sirius.getHeight()) {

    IJ.error("Images do not have matching dimensions!")

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
applyROI(sirius, labyrinthROI)
double labyrinthArea = measureArea(sirius, labyrinthROI)

println("Labyrinth Area = " + labyrinthArea + " pixels²")

ImagePlus labyrinthImage = duplicateROI(sirius, labyrinthROI)

labyrinthImage.show()

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
// CLEAN BINARY MASK
//----------------------------------------------------

ImagePlus cleanBinaryMask(IMagePlus mask){
	ImagePlus cleanedMask = mask.duplicate()
	IJ.run(cleanedMask, "Fill Holes", "")
	removeSmallParticles(cleanedMask, 20)
	
	return cleanedMask
	
}
//----------------------------------------------------
// REMOVE SMALL PARTICLES
//----------------------------------------------------

void removeSmallParticles(ImagePlus mask, int minArea){
	BinaryProcessor bp =
        new BinaryProcessor(mask.getProcessor().convertToByteProcessor())

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
// DUPLICARE ROI
//----------------------------------------------------

ImagePlus duplicateROI(ImagePlus image, Roi roi){

    image.setRoi(roi)

    Duplicator duplicator = new Duplicator()

    return duplicator.run(image)

}
//----------------------------------------------------
// PROCESS COMPARTMENT
//----------------------------------------------------

Roi processCompartment(ImagePlus classified, int classID){

    ImagePlus mask = createBinaryMask(classified, classID)

    ImagePlus cleanedMask = cleanBinaryMask(mask)

    Roi roi = createROI(cleanedMask)

    return roi

}
//----------------------------------------------------
// MEASURE AREA
//----------------------------------------------------

double measureArea(ImagePlus image, Roi roi){

    image.setRoi(roi)

    ImageStatistics stats = image.getStatistics()

    return stats.area

}