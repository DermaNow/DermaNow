
function species = image_process(in)

% prep

in_image = imread(in);

R_channel = double(in_image (:,:,1));
G_channel = double(in_image (:,:,2));
B_channel = double(in_image (:,:,3));

int_gray = .299* R_channel + .587 * G_channel + .114 * B_channel;

% Noise

filtered = ordfilt2(int_gray,9,ones(3,3));

rows = size(filtered,1);
colums = size(filtered,2);


% Segmentation

level = graythresh(uint8(filtered))*256;
bw = int_gray < level;

cleanerS = bwareaopen(bw, 150);
se= strel('disk', 5);
ioclosed = imclose(cleanerS, se);
iopen= imopen(ioclosed, se);

% preparing for feature extraction
final_image = zeros(rows,colums);
for (i=1:rows)
    for (j = 1:colums)
        if iopen(i,j)> 0
            final_image (i,j) = filtered(i,j);
        end
    end
end

% GLCM

GLCM2 = graycomatrix(final_image,'Offset',[2 0;0 2]);
stats = GLCM_Features1(GLCM2,0);

Contrast = stats.contr;
Corrption = stats.corrp;
Energy = stats.energ;
Homogeneity = stats.homom;
Mean = mean2(final_image);
Standard_Deviation = std2(final_image);
Entropy = entropy(final_image);
RMS = mean2(rms(final_image));

disease = [Contrast, Corrption, Energy, Homogeneity, Mean, Standard_Deviation,  Entropy, RMS];
save('inputImageTest.mat','disease');

% classify

xdata = [featuresE; featuresE1; featuresE2;featuresE3; featuresE4; featuresE5; featuresE6; 
         featuresE7; featuresE8; featuresE9;
         featuresM; featuresM1;featuresM2; featuresM3; featuresM4; featuresM5; featuresM6;
         featuresM7; featuresM8; featuresM9; featuresM10;] ;

group = ['Eczema__'; 'Eczema__'; 'Eczema__'; 'Eczema__'; 'Eczema__'; 'Eczema__'; 'Eczema__'; 
         'Eczema__'; 'Eczema__'; 'Eczema__';
         'Melanoma'; 'Melanoma';'Melanoma'; 'Melanoma'; 'Melanoma';'Melanoma'; 'Melanoma'
         'Melanoma'; 'Melanoma'; 'Melanoma'; 'Melanoma';];
     
svmStruct = svmtrain(xdata,group,'ShowPlot',true);


%svmStruct = load('svmStruct.mat');

testFile = load('inputImageTest.mat');
test=testFile.disease;


Xnew = [test];
species = svmclassify(svmStruct,Xnew)


end


