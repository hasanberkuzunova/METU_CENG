N=4145;     
lmbdauto1 = 50;   
lmbdauto2 = 0.15;  
alphaauto = 190;
lmbdtruck1 = 10;   
lmbdtruck2 = 0.01;
alphatruck = 110;


TotalWeight=zeros(N,1);

for k=1:N;

	U = rand; i = 0; 
	F = exp(-lmbdauto1);
	while (U>=F);
		i=i+1;
		F = F+exp(-lmbdauto1)*lmbdauto1^i/gamma(i+1);
	end;
	NCars = i;   

	U = rand; i = 0; 
	F = exp(-lmbdtruck1);
	while (U>=F);
		i=i+1;
		F = F+exp(-lmbdtruck1)*lmbdtruck1^i/gamma(i+1);
	end;
	NTrucks = i;   
	

	WAutos = 0;
	for j=1:NCars;
		WAutos = WAutos + sum(-1/lmbdauto2 * log(rand(alphaauto, 1)));
	end;

	WTrucks = 0;
	for j=1:NTrucks;
		WTrucks = WTrucks + sum(-1/lmbdtruck2 * log(rand(alphatruck, 1)));
	end;

	TotalWeight(k) = WAutos + WTrucks;
end;
p_est = mean(TotalWeight>200000);
expectedWeight = mean(TotalWeight);
stdWeight = std(TotalWeight);
fprintf('Estimated probability = %f\n',p_est);
fprintf('Expected weight = %f\n',expectedWeight);
fprintf('Standard deviation = %f\n',stdWeight);