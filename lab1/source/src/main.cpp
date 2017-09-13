/**
* GTI770 - Systemes intelligents et apprentissage machine
* Alessandro L. Koerich
* alessandro.koerich@etsmtl.ca
* 2015
*
* EXEMPLE 1: Feature extraction from RGB images
*                       Simpsons Family
**/

// INCLUDES
#include <cv.h> 			//OpenCV lib
#include <cvaux.h>			//OpenCV lib
#include <highgui.h>		//OpenCV lib
#include <stdio.h>	
#include <math.h>
#include <string>


// DEFINES
#define NUM_SAMPLES 100
#define NUM_FEATURES 5

const bool train = true;
const bool showImg = true;
const int numCharacters = 2;

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

Character characters[4] =
{
	Character("bart", "Bart", 80, 54),
	Character("homer", "Homer", 62, 37),
	Character("lisa", "Lisa", 0, 0),
	Character("other", "Unknown", 0, 0)
};


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

	// Feature vector [rows] [columns]
	// In fact it is a "matrix of features"
	float fVector[NUM_FEATURES];

	// Feature variables
	float fOrange;
	float fWhite;

	// OpenCV variables related to the image structure.
	// IplImage structure contains several information of the image (See OpenCV manual).	
	IplImage *img = NULL;
	IplImage *processed = NULL;
	IplImage *threshold = NULL;

	// OpenCV variable that stores the image width and height
	CvSize tam;

	// OpenCV variable that stores a pixel value
	CvScalar element;

	// Fill fVector with zeros
	
	for (jj = 0; jj < NUM_FEATURES; jj++)
	{
		fVector[jj] = 0.0;
	}

	printf(" %s\n", fName);

	// Load the image from disk to the structure img.
	// 1  - Load a 3-channel image (color)
	// 0  - Load a 1-channel image (gray level)
	// -1 - Load the image as it is  (depends on the file)

	img = cvLoadImage(fName, -1);

	// Gets the image size (width, height) 'img' 
	tam = cvGetSize(img);

	// Creates a header and allocates memory (tam) to store a copy of the original image.
	// 1 - gray level image
	// 3 - color image	
	// processed = cvCreateImage( tam, IPL_DEPTH_8U, 3);

	// Make a image clone and store it at processed and threshold
	processed = cvCloneImage(img);
	threshold = cvCloneImage(img);

	// Initialize variables with zero 
	fOrange = 0.0;
	fWhite = 0.0;

	// Loop that reads each image pixel
	for (h = 0; h < img->height; h++) // rows
	{
		for (w = 0; w < img->width; w++) // columns
		{
			// Read each channel and writes it into the blue, green and red variables. Notice that OpenCV considers BGR
			blue = ((uchar *)(img->imageData + h*img->widthStep))[w*img->nChannels + 0];
			green = ((uchar *)(img->imageData + h*img->widthStep))[w*img->nChannels + 1];
			red = ((uchar *)(img->imageData + h*img->widthStep))[w*img->nChannels + 2];

			// Shows the pixel value at the screen
			//printf( "pixel[%d][%d]= %d %d %d \n", h, w, (int)blue, (int)green, (int)red );

			// Here starts the feature extraction....

			// Detect and count the number of orange pixels
			// Verify if the pixels have a given value ( Orange, defined as R[240-255], G[85-105], B[11-22] ). If so, count it...
			if (blue >= 11 && blue <= 22 && green >= 85 && green <= 105 && red >= 240 && red <= 255)
			{
				fOrange++;

				// Just to be sure we are doing the right thing, we change the color of the orange pixels to green [R=0, G=255, B=0] and show them into a cloned image (processed)

				((uchar *)(processed->imageData + h*processed->widthStep))[w*processed->nChannels + 0] = 0;
				((uchar *)(processed->imageData + h*processed->widthStep))[w*processed->nChannels + 1] = 255;
				((uchar *)(processed->imageData + h*processed->widthStep))[w*processed->nChannels + 2] = 0;
			}

			// Detect and count the number of white pixels (just a dummy feature...)
			// Verify if the pixels have a given value ( White, defined as R[253-255], G[253-255], B[253-255] ). If so, count it...
			if (blue >= 253 && green >= 253 && red >= 253)
			{
				fWhite++;
			}

			// Here you can add your own features....... Good luck

		}
	}

	// Lets make our counting somewhat independent on the image size...
	// Compute the percentage of pixels of a given colour.
	// Normalize the feature by the image size
	fOrange = fOrange / ((int)img->height * (int)img->width);
	fWhite = fWhite / ((int)img->height * (int)img->width);

	// Store the feature value in the columns of the feature (matrix) vector
	fVector[1] = fOrange;
	fVector[2] = fWhite;

	// Here you can add more features to your feature vector by filling the other columns: 
	//   fVector[3] = ???; fVector[4] = ???;

	// And finally, store your features in a file

	static char output[255] = { '\0' };
	// Fill output with zeros
	for (ii = 0; ii < 255; ii++)
	{
		output[ii] = '\0';
	}

	// TODO Add 3, 4, 5 (new primitives)
	sprintf(output, "%f,%f,%s", fVector[1], fVector[2], character.label);

	// Finally, give a look at the original image and the image with the pixels of interest in green
	// OpenCV create an output window
	if (showImg)
	{
		cvShowImage("Original", img);
		cvShowImage("Processed", processed);

		// Wait until a key is pressed to continue... 	
		tecla = cvWaitKey(0);
	}

	cvReleaseImage(&img);
	cvDestroyWindow("Original");

	cvReleaseImage(&processed);
	cvDestroyWindow("Processed");

	return output;
}

// MAIN
int main( int argc, char** argv )   
{
	int ii;
	int jj;
	int numSamples;

	// Variable filename
	static char cFileName[50] = { '\0' };
	FILE *fp;
	
	// Open a text file to store the feature vectors
	fp = fopen ((train ? "apprentissage-homer-bart.arff" : "validation-homer-bart.arff"),"w");

	if(fp == NULL) {
		perror((train ? "failed to open apprentissage-homer-bart.arff" : "failed to open validation-homer-bart.arff"));
		return EXIT_FAILURE;
	}

	// File initialization for Weka
	fprintf(fp, "@relation Homer-Bart\n\n");
	fprintf(fp, "@attribute primitive1 numeric\n");
	fprintf(fp, "@attribute primitive2 numeric\n");
	fprintf(fp, "@attribute classe {Homer, Bart}\n\n");
	fprintf(fp, "@data\n\n");

	// Fill cFileName with zeros
	for ( ii = 0 ; ii < 50 ; ii++ )
	{
		cFileName[ ii ] = '\0';
	}

	char* result;

	for (ii = 0; ii < numCharacters; ii++)
	{
		Character c = characters[ii];
		numSamples = (train ? c.numTrain : c.numValid);

		for (jj = 1; jj <= numSamples; jj++)
		{
			sprintf(cFileName, "%s/%s%d.bmp", (train ? "Train" : "Valid"), c.name, jj);
			result = checkImage(cFileName, c);

			printf("%s\n", result);
			fprintf(fp, "%s\n", result);
		}
	}

	fclose(fp);

	return 0;
} 
