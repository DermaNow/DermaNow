


FileMat=dir('.mat');
numFiles = length(FileMat);

%Acne
acne=load('featuresAcne.mat');
featuresA=acne.feat_disease;

acne1=load('featuresAcne1.mat');
featuresA1=acne1.feat_disease;

acne2=load('featuresAcne2.mat');
featuresA2=acne2.feat_disease;

acne3=load('featuresAcne3.mat');
featuresA3=acne3.feat_disease;

acne4=load('featuresAcne4.mat');
featuresA4=acne4.feat_disease;

acne5=load('featuresAcne5.mat');
featuresA5=acne5.feat_disease;

acne6=load('featuresAcne6.mat');
featuresA6=acne6.feat_disease;

acne19=load('featuresAcne19.mat');
featuresA19=acne19.feat_disease;

acne20=load('featuresAcne20.mat');
featuresA20=acne20.feat_disease;

acne24=load('featuresAcne24.mat');
featuresA24=acne24.feat_disease;

acne27=load('featuresAcne27.mat');
featuresA27=acne27.feat_disease;

% Melanoma
melanoma =load('featuresMelo.mat');
featuresM=melanoma.feat_disease;

melanoma1= load('featuresMelo2.mat');
featuresM1=melanoma1.feat_disease;


melanoma2= load('featuresMelo3.mat');
featuresM2=melanoma2.feat_disease;


melanoma3= load('featuresMelo5.mat');
featuresM3=melanoma3.feat_disease;

melanoma4= load('featuresMelo6.mat');
featuresM4=melanoma4.feat_disease;


melanoma5= load('featuresMelo7.mat');
featuresM5=melanoma5.feat_disease;


melanoma6= load('featuresMelo8.mat');
featuresM6=melanoma6.feat_disease;

melanoma7= load('featuresMelo9.mat');
featuresM7=melanoma7.feat_disease;

melanoma8= load('featuresMelo10.mat');
featuresM8=melanoma8.feat_disease;

melanoma9= load('featuresMelo11.mat');
featuresM9=melanoma9.feat_disease;

melanoma10= load('featuresMelo12.mat');
featuresM10=melanoma10.feat_disease;

% Eczema 
eczema=load('featureEczema.mat');
featuresE=eczema.feat_disease;

eczema1=load('featureEczema1.mat');
featuresE1=eczema1.feat_disease;

eczema2=load('featureEczema2.mat');
featuresE2=eczema2.feat_disease;

eczema3=load('featureEczema3.mat');
featuresE3=eczema3.feat_disease;

eczema4=load('featureEczema4.mat');
featuresE4=eczema4.feat_disease;

eczema5=load('featureEczema5.mat');
featuresE5=eczema5.feat_disease;

eczema6=load('featureEczema6.mat');
featuresE6=eczema6.feat_disease;


eczema7=load('featureEczema7.mat');
featuresE7=eczema7.feat_disease;

eczema8=load('featureEczema8.mat');
featuresE8=eczema8.feat_disease;

eczema9=load('featureEczema9.mat');
featuresE9=eczema9.feat_disease;



xdata = [featuresE; featuresE1; featuresE2;featuresE3; featuresE4; featuresE5; featuresE6; 
         featuresE7; featuresE8; featuresE9;
         featuresM; featuresM1;featuresM2; featuresM3; featuresM4; featuresM5; featuresM6;
         featuresM7; featuresM8; featuresM9; featuresM10;] ;

group = ['Eczema__'; 'Eczema__'; 'Eczema__'; 'Eczema__'; 'Eczema__'; 'Eczema__'; 'Eczema__'; 
         'Eczema__'; 'Eczema__'; 'Eczema__';
         'Melanoma'; 'Melanoma';'Melanoma'; 'Melanoma'; 'Melanoma';'Melanoma'; 'Melanoma'
         'Melanoma'; 'Melanoma'; 'Melanoma'; 'Melanoma';];
     
svmStruct = svmtrain(xdata,group,'ShowPlot',true);

save('svmStruct.mat','svmStruct');

% save svmStruct 
% learned = load svmStruct 
% in the species method we use leaned instead of svmStruct 

testFile = load('melo.mat');
test=testFile.feat_disease;

Xnew = [test];
species = svmclassify(svmStruct,Xnew)
%hold on;
%plot(Xnew(:,1),Xnew(:,2),'ro','MarkerSize',12);
%hold off

  

