// Constants for the different tests
#define HB 0
#define HBL 1
#define HBLO 2

// INCLUDES
#include <cv.h> 				// OpenCV lib
#include <cvaux.h>				// OpenCV lib
#include <highgui.h>			// OpenCV lib
#include <stdio.h>	
#include <math.h>
#include <string>

const bool train = false;		// Whether we use the training data set
const int testCode = HB;		// Test to perform HB, HBL, HBLO
const bool showImg = false;		// Whether we should display the images

/*
 Holds information on the different tests that we can perform
*/
struct Test
{
	char* trainFileName;
	char* validFileName;
	int numCharacters;

	/* Constructor */
	Test(char* tFileName, char* vFileName, int numChars)
	{
		trainFileName = tFileName;
		validFileName = vFileName;
		numCharacters = numChars;
	}
};

/*
  Holds information on each character in the training and validation sets
 */
struct Character
{
	char* name;
	char* label;
	int numTrain;
	int numValid;

	/* Constructor */
	Character(char* pname, char* plabel, int pnumTrain, int pnumValid)
	{
		name = pname;
		label = plabel;
		numTrain = pnumTrain;
		numValid = pnumValid;
	}
};

Test tests[3] =
{
	// Homer - Bart
	Test("EquipeC-Apprentissage-HB.arff", "EquipeC-Validation-HB.arff", 2),
	// Homer - Bart - Lisa
	Test("EquipeC-Apprentissage-HBL.arff", "EquipeC-Validation-HBL.arff", 3),
	// Homer - Bart - Lisa - Others
	Test("EquipeC-Apprentissage-HBLO.arff", "EquipeC-Validation-HBLO.arff", 5)
};

Character characters[8] =
{
	Character("bart", "Bart", 80, 115),
	Character("homer", "Homer", 62, 87),
	Character("lisa", "Lisa", 33, 46),
	Character("other", "Other", 121, 170),
	Character("school", "Other", 35, 49),
	Character("family", "Other", 27, 38),
	Character("maggie", "Maggie", 30, 42),
	Character("marge", "Marge", 24, 34)
};

/* Detects white in a pixel */
bool isWhite(cv::Vec3b color)
{
	int blue = color[0];
	int green = color[1];
	int red = color[2];
	return blue >= 253 && green >= 253 && red >= 253;
}

/* Detects brown in a pixel */
bool isBrown(cv::Vec3b color)
{
	int blue = color[0];
	int green = color[1];
	int red = color[2];
	return blue >= 95 && blue <= 135 && green >= 150 && green <= 185 && red >= 180 && red <= 210;
}

/* Detects orange in a pixel */
bool isOrange(cv::Vec3b color)
{
	int blue = color[0];
	int green = color[1];
	int red = color[2];
	return blue >= 11 && blue <= 22 && green >= 85 && green <= 105 && red >= 240 && red <= 255;
}

/* Detects orange in a pixel */
bool isYellow(cv::Vec3b color)
{
	int blue = color[0];
	int green = color[1];
	int red = color[2];
	return blue <= 70 && green >= 180 && green <= 220 && red >= 230 && red <= 255;
}

/* Detects blue in a pixel */
bool isBlue(cv::Vec3b color)
{
	int blue = color[0];
	int green = color[1];
	int red = color[2];
	return blue >= 150 && blue <= 190 && green >= 85 && green <= 130 && red <= 90;
}

/* Detects purple in a pixel */
bool isPurple(cv::Vec3b color) 
{
	int blue = color[0];
	int green = color[1];
	int red = color[2];
	return blue >= 50 && blue <= 150 && green >= 0 && green <= 50 && red <= 50;
}

/* Detects red in a pixel */
bool isRed(cv::Vec3b color)
{
	int blue = color[0];
	int green = color[1];
	int red = color[2];
	return blue <= 5 && green <= 5 && red >= 250;
}

/* Detects light blue in a pixel */
bool isLightBlue(cv::Vec3b color)
{
	int blue = color[0];
	int green = color[1];
	int red = color[2];
	return blue <= 50 && green >= 135 && green <= 170 && red >= 200;
}

/* Detects green in a pixel */
bool isGreen(cv::Vec3b color)
{
	int blue = color[0];
	int green = color[1];
	int red = color[2];
	return blue >= 145 && blue <= 160 && green >= 170 && green <= 200 && red <= 40;
}

/* Normalizes a pixel count into a ratio */
float normalize(float val, float fYellow)
{
	// Ratio of numColor / (numColor + numYellow + 1)
	// The +1 was added to prevent division by zero
	return val / (val + fYellow + 1);
}

/* Gets the maximum out of three numbers */
float getMax(float val1, float val2, float val3)
{
	float max = val1;
	if (val2 > max)
		max = val2;
	if (val3 > max)
		max = val3;
	return max;
}

/* Counts color ratios in an image to detect primitives */
char* checkImage(char* fName, Character character)
{
	// Variable store pressed key
	int tecla;

	// General variables (loop)
	int h;
	int w;
	int ii;
	int jj;

	// Variables to store the RGB values of a pixel
	unsigned char red;
	unsigned char blue;
	unsigned char green;

	// Feature variables
	float fOrange;
	float fWhite;
	float fBlue;
	float fPurple;
	float fBrown;
	float fRed;
	float fLightBlue;
	float fGreen;
	float fYellow;
	float fMax;

	// Images
	cv::Mat img;
	cv::Mat processed;

	// Fill fVector with zeros
	
	printf(" %s\n", fName);

	// Load the image from disk to the structure img.
	// 1  - Load a 3-channel image (color)
	// 0  - Load a 1-channel image (gray level)
	// -1 - Load the image as it is  (depends on the file)

	img = cv::imread(fName, 1);

	// Make a image clone and store it at processed and threshold
	processed = img.clone();

	// Initialize variables with zero 
	fOrange = 0.0;
	fWhite = 0.0;
	fBlue = 0.0;
	fPurple = 0.0;
	fBrown = 0.0;
	fRed = 0.0;
	fLightBlue = 0.0;
	fGreen = 0.0;
	fYellow = 0.0;
	fMax = 0.0;

	// Loop that reads each image pixel
	for (h = 0; h < img.rows; h++) // rows
	{
		for (w = 0; w < img.cols; w++) // columns
		{
			cv::Vec3b color = img.at<cv::Vec3b>(h, w);

			// Detect and count the number of orange pixels
			if (isOrange(color))
			{
				fOrange++;
			}

			// Detect and count the number of white pixels (just a dummy feature...)
			else if (isWhite(color))
			{
				fWhite++;
			}

			// Detect and count the number of blue pixels
			else if (isBlue(color))
			{
				fBlue++;

				color = cv::Vec3b(255, 0, 255);
				processed.at<cv::Vec3b>(h, w) = color;
			}

			// Detect and count the number of purple pixels
			else if (isPurple(color))
			{
				fPurple++;

				color = cv::Vec3b(255, 255, 0);
				processed.at<cv::Vec3b>(h, w) = color;
			}

			// Detect and count the number of brown pixels
			else if (isBrown(color))
			{
				fBrown++;
			}

			// Detect and count the number of red pixels
			else if (isRed(color))
			{
				fRed++;
			}

			// Detect and count the number of light blue pixels
			else if (isLightBlue(color))
			{
				fLightBlue++;
			}

			// Detect and count the number of green pixels
			else if (isGreen(color))
			{
				fGreen++;
			}

			// Detect and count the number of yellow pixels
			else if (isYellow(color))
			{
				fYellow++;
				color = cv::Vec3b(0, 0, 255);
				processed.at<cv::Vec3b>(h, w) = color;
			}

		}
	}

	//float normalizer = (img.rows * img.cols);
	float normalizer = fYellow + 1;

	fOrange = normalize(fOrange, fYellow);
	fWhite = normalize(fWhite, fYellow);
	fBlue = normalize(fBlue, fYellow);
	fPurple = normalize(fPurple, fYellow);
	fBrown = normalize(fBrown, fYellow);
	fRed = normalize(fRed, fYellow);
	fLightBlue = normalize(fLightBlue, fYellow);
	fGreen = normalize(fGreen, fYellow);
	fMax = getMax(fOrange, fBrown, fRed);

	// And finally, store your features in a file

	static char output[255] = { '\0' };
	// Fill output with zeros
	for (ii = 0; ii < 255; ii++)
	{
		output[ii] = '\0';
	}

	if (testCode == HB)
		sprintf(output, "%f,%f,%f,%f,%f,%s", fOrange, fWhite, fBlue, fPurple, fBrown, character.label);
	else if(testCode == HBL)
		sprintf(output, "%f,%f,%f,%f,%f,%f,%s", fOrange, fWhite, fBlue, fPurple, fBrown, fRed, character.label);
	else
		sprintf(output, "%f,%f,%f,%f,%f,%f,%f,%s", fOrange, fWhite, fBlue, fPurple, fBrown, fRed, fMax, character.label);
		
	

	// Finally, give a look at the original image and the image with the pixels of interest in green
	// OpenCV create an output window
	if (showImg)
	{
		cv::imshow("Original", img);
		cv::imshow("Processed", processed);

		// Wait until a key is pressed to continue... 	
		tecla = cv::waitKey(0);
	}

	img.release();
	processed.release();

	cv::destroyAllWindows();

	return output;
}

/* For each image, performs data processing */
int performTraining()
{
	int ii;
	int jj;
	int numSamples;

	// Variable filename
	static char cFileName[50] = { '\0' };
	FILE *fp;
	
	// Open a text file to store the feature vectors
	fp = fopen ((train ? tests[testCode].trainFileName : tests[testCode].validFileName),"w");

	if(fp == NULL) {
		perror((train ? "failed to open apprentissage...arff" : "failed to open validation...arff"));
		return EXIT_FAILURE;
	}

	// File initialization for Weka

	if(testCode == HB)
		fprintf(fp, "@relation Homer-Bart\n\n");
	else if (testCode == HBL)
		fprintf(fp, "@relation Homer-Bart-Lisa\n\n");
	else if (testCode == HBLO)
		fprintf(fp, "@relation Homer-Bart-Lisa-Others\n\n");

	fprintf(fp, "@attribute colorOrange numeric\n");
	fprintf(fp, "@attribute colorWhite numeric\n");
	fprintf(fp, "@attribute colorBlue numeric\n");
	fprintf(fp, "@attribute colorPurple numeric\n");
	fprintf(fp, "@attribute colorBrown numeric\n");
	
	if (testCode > HB)
	{
		fprintf(fp, "@attribute colorRed numeric\n");
		if (testCode > HBL)
		{
			fprintf(fp, "@attribute maxDeterminant numeric\n");
		}
	}
	fprintf(fp, "@attribute classe {Homer, Bart, Lisa, Other}\n\n");
	fprintf(fp, "@data\n\n");

	// Fill cFileName with zeros
	for ( ii = 0 ; ii < 50 ; ii++ )
	{
		cFileName[ ii ] = '\0';
	}

	char* result;

	for (ii = 0; ii < tests[testCode].numCharacters; ii++)
	{
		Character c = characters[ii];
		numSamples = (train ? c.numTrain : c.numValid);

		for (jj = (train ? 1 : c.numTrain + 1); jj <= numSamples; jj++)
		{
			sprintf(cFileName, "%s/%s%03d.bmp", (train ? "Train" : "Valid"), c.name, jj);
			
			// Send image for processing
			result = checkImage(cFileName, c);

			// Print results to screen and file
			printf("%s\n", result);
			fprintf(fp, "%s\n", result);
		}
	}

	fclose(fp);

	return 0;
} 

/* Main method */
int main(int argc, char** argv)
{
	int result = performTraining();

	return result;
}
