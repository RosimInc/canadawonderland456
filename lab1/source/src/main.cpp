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
#define NUM_FEATURES 8

const bool train = true;		// Whether we use the training data set
const bool showImg = false;		// Whether we display the images
const int numCharacters = 2;	// Number of characters (2, 3, 5)

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


bool isWhite(cv::Vec3b color)
{
	int blue = color[0];
	int green = color[1];
	int red = color[2];
	return blue >= 253 && green >= 253 && red >= 253;
}

/*
 * TODO:
 * With this RGB range, we find that some Bart images have brown color. 
 * To lessen false positives, either combine brown and blue primitives or reduce the brown RGB range detection
 */
bool isBrown(cv::Vec3b color)
{
	int blue = color[0];
	int green = color[1];
	int red = color[2];
	return blue >= 95 && blue <= 135 && green >= 150 && green <= 185 && red >= 180 && red <= 210;
}

bool isOrange(cv::Vec3b color)
{
	int blue = color[0];
	int green = color[1];
	int red = color[2];
	return blue >= 11 && blue <= 22 && green >= 85 && green <= 105 && red >= 240 && red <= 255;
}

bool isYellow(cv::Vec3b color)
{
	int blue = color[0];
	int green = color[1];
	int red = color[2];
	return blue <= 11 && green >= 180 && green <= 200 && red >= 230 && red <= 255;
}

bool isBlue(cv::Vec3b color)
{
	int blue = color[0];
	int green = color[1];
	int red = color[2];
	// TODO: Improve treshold, blue can currently be grey
	return blue >= 128 && green <= 128 && red <= 128;
}

bool isRed(cv::Vec3b color)
{
	int blue = color[0];
	int green = color[1];
	int red = color[2];
	return blue <= 5 && green <= 5 && red >= 250;
}

bool isLightBlue(cv::Vec3b color)
{
	int blue = color[0];
	int green = color[1];
	int red = color[2];
	return blue <= 50 && green >= 135 && green <= 170 && red >= 200;
}

bool isGreen(cv::Vec3b color)
{
	int blue = color[0];
	int green = color[1];
	int red = color[2];
	return blue >= 145 && blue <= 160 && green >= 170 && green <= 200 && red <= 40;
}

void findLines(cv::Mat img) 
{
	std::vector<cv::Vec4i> lines;
	lines.reserve(10000);
	cv::Mat mat = img.clone();
	// detect the lines
	//IplImage *gray = NULL;
	//gray = cvLoadImage(fName, CV_LOAD_IMAGE_GRAYSCALE);
	// TODO: Test cornerHarris

	cv::cvtColor(mat, mat, cv::COLOR_RGB2GRAY);
	cv::threshold(mat, mat, 15, 255, cv::THRESH_BINARY_INV);

	//cv::Canny(mat, mat, 10, 100, 3);
	//cv::dilate(mat, mat, cv::Mat(), cv::Point(-1, -1), 2, 1, 1);
	//cv::imshow("tes2t", mat);
	cv::HoughLinesP(mat, lines, 1, CV_PI / 180, 20, 5, 5);


	cv::Mat newImg = cv::Mat(img.rows, img.cols, CV_8U, cv::Scalar(255));

	printf("Size %d\n", lines.size());
	for (size_t i = 0; i < lines.size(); i++)
	{
		cv::Vec4i l = lines[i];
		// draw the lines

		cv::Point p1, p2;
		p1 = cv::Point(l[0], l[1]);
		p2 = cv::Point(l[2], l[3]);

		cv::line(newImg, p1, p2, 0, 1, 8, 0);
		//printf("Point(%d, %d) - Point(%d, %d)\n", l[0], l[1], l[2], l[3]);
     }

	int thresh = 200;
	cv::Mat dst, dst_norm, dst_norm_scaled;
	dst = cv::Mat::zeros(newImg.size(), CV_32FC1);

	// Detecting corners
	cv::cornerHarris(mat, dst, 7, 5, 0.05, cv::BORDER_DEFAULT);

	// Normalizing
	cv::normalize(dst, dst_norm, 0, 255, cv::NORM_MINMAX, CV_32FC1, cv::Mat());
	cv::convertScaleAbs(dst_norm, dst_norm_scaled);

	// Drawing a circle around corners
	for (int j = 0; j < dst_norm.rows; j++)
	{
		for (int i = 0; i < dst_norm.cols; i++)
		{
			if ((int)dst_norm.at<float>(j, i) > thresh)
			{
				circle(dst_norm_scaled, cv::Point(i, j), 5, cv::Scalar(255,0,0), 1, 8, 0);
			}
		}
	}

	//imshow("corners_window", dst_norm_scaled);

	//cv::imshow("test", newImg);
	//cv::waitKey(0);
}

float normalize(float val, float fYellow)
{
	return val / (val + fYellow + 1);
}

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
	float fBlue;
	float fBrown;
	float fRed;
	float fLightBlue;
	float fGreen;
	float fYellow;

	cv::Mat img;
	cv::Mat processed;
	cv::Mat threshold;
	cv::Mat gray;
	cv::Mat bw;

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

	img = cv::imread(fName, 1);

	// Make a image clone and store it at processed and threshold
	processed = img.clone();
	threshold = img.clone();
	cvtColor(img, gray, CV_BGR2GRAY);
	cv::threshold(gray, bw, 230, 255, CV_THRESH_BINARY);

	/// Reduce the noise so we avoid false circle detection
	cv::GaussianBlur(gray, gray, cv::Size(9, 9), 2, 2);

	cv::vector<cv::Vec3f> circles;

	/// Apply the Hough Transform to find the circles
	cv::HoughCircles(bw, circles, CV_HOUGH_GRADIENT, 1, bw.rows / 8, 200, 100, 0, 0);

	for (size_t i = 0; i < circles.size(); i++)
	{ 
		cv::Point center(cvRound(circles[i][0]), cvRound(circles[i][1]));
		int radius = cvRound(circles[i][2]);
		// circle center
		cv::circle(processed, center, 3, cv::Scalar(0, 255, 0), -1, 8, 0);
		// circle outline
		cv::circle(processed, center, radius, cv::Scalar(0, 0, 255), 3, 8, 0);
	}

	// Initialize variables with zero 
	fOrange = 0.0;
	fWhite = 0.0;
	fBlue = 0.0;
	fBrown = 0.0;
	fRed = 0.0;
	fLightBlue = 0.0;
	fGreen = 0.0;
	fYellow = 0.0;

	findLines(img);

	// Loop that reads each image pixel
	for (h = 0; h < img.rows; h++) // rows
	{
		for (w = 0; w < img.cols; w++) // columns
		{
			cv::Vec3b color = img.at<cv::Vec3b>(h, w);

			// Here starts the feature extraction....

			// Detect and count the number of orange pixels
			// Verify if the pixels have a given value ( Orange, defined as R[240-255], G[85-105], B[11-22] ). If so, count it...
			if (isOrange(color))
			{
				fOrange++;

				/*color = cv::Vec3b(128, 128, 128);
				processed.at<cv::Vec3b>(h, w) = color;*/
			}

			// Detect and count the number of white pixels (just a dummy feature...)
			// Verify if the pixels have a given value ( White, defined as R[253-255], G[253-255], B[253-255] ). If so, count it...
			else if (isWhite(color))
			{
				fWhite++;

				/*color = cv::Vec3b(0, 255, 0);
				processed.at<cv::Vec3b>(h, w) = color;*/
			}

			// Here you can add your own features....... Good luck

			// Detect and count the number of blue pixels
			else if (isBlue(color))
			{
				fBlue++;

				/*color = cv::Vec3b(255, 0, 255);
				processed.at<cv::Vec3b>(h, w) = color;*/
			}

			// Detect and count the number of brown pixels
			else if (isBrown(color))
			{
				fBrown++;

				/*color = cv::Vec3b(255, 255, 0);
				processed.at<cv::Vec3b>(h, w) = color;*/
			}

			// Detect and count the number of red pixels
			else if (isRed(color))
			{
				fRed++;

				/*color = cv::Vec3b(0, 255, 255);
				processed.at<cv::Vec3b>(h, w) = color;*/
			}

			// Detect and count the number of light blue pixels
			else if (isLightBlue(color))
			{
				fLightBlue++;

				/*color = cv::Vec3b(128, 128, 0);
				processed.at<cv::Vec3b>(h, w) = color;*/
			}

			// Detect and count the number of green pixels
			else if (isGreen(color))
			{
				fGreen++;

				/*color = cv::Vec3b(0, 128, 128);
				processed.at<cv::Vec3b>(h, w) = color;*/
			}
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
	fBrown = normalize(fBrown, fYellow);
	fRed = normalize(fRed, fYellow);
	fLightBlue = normalize(fLightBlue, fYellow);
	fGreen = normalize(fGreen, fYellow);

	// Store the feature value in the columns of the feature (matrix) vector
	fVector[1] = fOrange;
	fVector[2] = fWhite;

	// Here you can add more features to your feature vector by filling the other columns: 
	//   fVector[3] = ???; fVector[4] = ???;
	fVector[3] = fBlue;
	fVector[4] = fBrown;
	fVector[5] = fRed;
	fVector[6] = fLightBlue;
	fVector[7] = fGreen;

	// And finally, store your features in a file

	static char output[255] = { '\0' };
	// Fill output with zeros
	for (ii = 0; ii < 255; ii++)
	{
		output[ii] = '\0';
	}

	// TODO Add 3, 4, 5 (new primitives) --- %f,%f,%f,
	sprintf(output, "%f,%f,%f,%f,%s", fVector[1], fVector[2], fVector[3], fVector[4]/*, fVector[5], fVector[6], fVector[7]*/, character.label);


	

	// Finally, give a look at the original image and the image with the pixels of interest in green
	// OpenCV create an output window
	if (showImg)
	{
		cv::imshow("Original", img);
		cv::imshow("Processed", processed);
		//cv::imshow("BW", bw);

		// Wait until a key is pressed to continue... 	
		tecla = cv::waitKey(0);
	}

	/*img.release();
	processed.release();
	threshold.release();
	gray.release();
	bw.release();*/

	cv::destroyAllWindows();

	return output;
}

/*void checkCircles(char* fName)
{
	// Variable store pressed key
	int tecla;

	// OpenCV variables related to the image structure.
	// IplImage structure contains several information of the image (See OpenCV manual).	
	IplImage *img = NULL;
	IplImage *gray = NULL;
	IplImage *processed = NULL;
	IplImage *threshold = NULL;

	// OpenCV variable that stores the image width and height
	CvSize tam;

	// OpenCV variable that stores a pixel value
	CvScalar element;

	// Fill fVector with zeros

	printf(" %s\n", fName);

	// Load the image from disk to the structure img.
	// 1  - Load a 3-channel image (color)
	// 0  - Load a 1-channel image (gray level)
	// -1 - Load the image as it is  (depends on the file)

	img = cvLoadImage(fName, CV_LOAD_IMAGE_UNCHANGED);
	gray = cvLoadImage(fName, CV_LOAD_IMAGE_GRAYSCALE);

	// Gets the image size (width, height) 'img' 
	tam = cvGetSize(img);
	processed = cvCloneImage(img);
	threshold = cvCloneImage(gray);

	cvThreshold(gray, threshold, 230, 255, CV_THRESH_BINARY_INV);

	float kernel[] = { 0.f,  0.2f, 0.f,
		0.2f,  0.2f, 0.2f,
		0.f, 0.2f, 0.f };

	//Vertical filter

	CvMat kernel_ = cvMat(3, 3, CV_32FC1, kernel);

	//cvConvertImage(gray, threshold, CV_GRAY2BGR);
	//cvFilter2D(gray, threshold, &kernel_, cvPoint(-1, -1));

	
	///////
	CvMemStorage* storage = cvCreateMemStorage(0);
	CvSeq* results = cvHoughCircles(
		threshold,
		storage,
		CV_HOUGH_GRADIENT,
		1,
		threshold->width / 60,
		100, 35, 1, 20
		);
	///////

	///////
	for (size_t i = 0; i < results->total; i++)
	{
		float* p = (float*)cvGetSeqElem(results, i);
		CvPoint pt = cvPoint(cvRound(p[0]), cvRound(p[1]));
		
		cvCircle(
			processed,
			pt,
			cvRound(p[2]),
			CV_RGB(0, 0, 0xff),
			2
			);
	}
	////////

	// Finally, give a look at the original image and the image with the pixels of interest in green
	// OpenCV create an output window
	if (showImg)
	{
		cvShowImage("Original", img);
		cvShowImage("Processed", processed);
		cvShowImage("Threshold", threshold);

		// Wait until a key is pressed to continue... 	
		tecla = cvWaitKey(0);
	}

	cvReleaseImage(&img);
	cvDestroyWindow("Original");

	cvReleaseImage(&processed);
	cvDestroyWindow("Processed");

	cvReleaseImage(&processed);
	cvDestroyWindow("Threshold");
}*/


int performTraining()
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
	fprintf(fp, "@attribute colorOrange numeric\n");
	fprintf(fp, "@attribute colorWhite numeric\n");
	fprintf(fp, "@attribute colorBlue numeric\n");
	fprintf(fp, "@attribute colorBrown numeric\n");
	/*fprintf(fp, "@attribute colorRed numeric\n");
	fprintf(fp, "@attribute colorLightBlue numeric\n");, Lisa, Other
	fprintf(fp, "@attribute colorGreen numeric\n");*/
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

		jj = (train ? 1 : c.numTrain + 1);
		for (; jj <= numSamples; jj++)
		{
			sprintf(cFileName, "%s/%s%03d.bmp", (train ? "Train" : "Valid"), c.name, jj);
			result = checkImage(cFileName, c);

			printf("%s\n", result);
			fprintf(fp, "%s\n", result);
		}
	}

	fclose(fp);

	return 0;
} 


// MAIN
int main(int argc, char** argv)
{
	int result = 0;

	//checkCircles("Train/Lisa002.bmp");

	result = performTraining();

	return result;
}
