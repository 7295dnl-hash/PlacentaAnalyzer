# PlacentaAnalyzer

Automated ImageJ/Fiji workflow for quantifying placental fibrosis in Sirius Red–stained histological sections.

---

## Overview

PlacentaAnalyzer is an open-source image analysis project that streamlines the quantification of placental fibrosis using Fiji (ImageJ). The workflow is designed to reduce manual measurements, improve reproducibility, and provide a standardized method for analyzing histological images.

The project uses Trainable Weka Segmentation to classify tissue structures and automatically quantify fibrotic regions across placental sections.

---

## Scientific Background

Placental fibrosis is characterized by excessive extracellular matrix deposition within placental tissue and has been associated with pregnancy complications such as diabetes and preeclampsia. Accurate quantification of fibrosis is important for understanding placental remodeling and evaluating disease progression.

Traditional manual analysis is time-consuming and susceptible to observer variability. PlacentaAnalyzer aims to provide a reproducible computational workflow that automates fibrosis measurement while remaining accessible to researchers using Fiji/ImageJ.

---

## Features

Current features include:

- Automated fibrosis quantification using Fiji/ImageJ
- Trainable Weka Segmentation integration
- Organized project structure for reproducible analysis
- Support for histological image processing

Planned features include:

- Batch image processing
- Automatic ROI detection
- CSV summary generation
- Segmentation overlay export
- User-friendly parameter selection
- Additional stain compatibility

---

## Project Structure

```text
PlacentaAnalyzer/
│
├── Data/          # Raw histological images
├── Models/        # Trained Weka classifier models
├── Output/        # Analysis results
├── Scripts/       # ImageJ macros and scripts
├── TestImages/    # Example images for testing
└── README.md
```

---

## Requirements

Software required:

- Fiji (ImageJ)
- Trainable Weka Segmentation plugin
- Java Runtime Environment

---

## Installation

Clone the repository:

```bash
git clone https://github.com/7295dnl-hash/PlacentaAnalyzer.git
```

Open the project in Fiji and place any trained Weka classifier models in the `Models` directory.

---

## Usage

1. Open Fiji.
2. Load the analysis macro from the `Scripts` directory.
3. Select a trained Weka classifier.
4. Load one or more placental histology images.
5. Run the analysis.
6. Review the generated measurements and output images.

---

## Development Roadmap

- [ ] Build complete fibrosis analysis macro
- [ ] Batch process multiple images
- [ ] Export measurements to CSV
- [ ] Generate segmentation overlays
- [ ] Improve error handling
- [ ] Create graphical user interface (GUI)
- [ ] Add documentation for classifier training

---

## Contributing

Contributions, suggestions, and bug reports are welcome.

If you discover an issue or have an idea for improving the workflow, please open an Issue or submit a Pull Request.

---

## License

This project is released under the MIT License.

---

## Author

Developed by **Deborah Lopez**.

Created as part of computational image analysis research focused on automated placental fibrosis quantification using Fiji/ImageJ.