// PlacentaAnalyzer v0.1
// ROI extraction from Weka classified image
// Class IDs:
// 0 = Background
// 1 = Labyrinth
// 2 = Junctional Zone
// 3 = Excluded

import ij.IJ
import ij.plugin.frame.RoiManager

// ---------- USER SETTINGS ----------
int LABYRINTH_CLASS = 1
int JZ_CLASS = 2

// -----------------------------------

def classified = IJ.getImage()

if (classified == null) {
    IJ.error("No classified image is open.")
    return
}

// Create (or get) the ROI Manager
def rm = RoiManager.getInstance()
if (rm == null)
    rm = new RoiManager()

extractROI(classified, LABYRINTH_CLASS, "Labyrinth", rm)
extractROI(classified, JZ_CLASS, "JunctionalZone", rm)

IJ.showMessage("Finished!",
        "Labyrinth and Junction Zone ROIs added to ROI Manager.")


// ===================================================
// FUNCTION
// ===================================================

def extractROI(image, classID, name, rm){

    def mask = image.duplicate()

    // Keep only the requested class
    IJ.setThreshold(mask, classID, classID)
    IJ.run(mask, "Convert to Mask", "")

    // Clean mask
    IJ.run(mask, "Fill Holes", "")
    IJ.run(mask, "Open", "")
    IJ.run(mask, "Close", "")

    // Create ROI
    IJ.run(mask, "Create Selection", "")

    def roi = mask.getRoi()

    if (roi != null){

        roi.setName(name)

        rm.addRoi(roi)

    }else{

        IJ.log("Could not create ROI for " + name)

    }

    mask.close()

}