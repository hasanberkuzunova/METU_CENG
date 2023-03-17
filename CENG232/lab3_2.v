`timescale 1ns / 1ps 
module lab3_2(
			input[4:0] smartCode,
			input CLK, 
			input lab, //0:Digital, 1:Mera
			input [1:0] mode, //00:exit, 01:enter, 1x: idle 
			output reg [5:0] numOfStuInMera,
			output reg [5:0] numOfStuInDigital,
			output reg restrictionWarnMera,//1:show warning, 0:do not show warning
			output reg isFullMera, //1:full, 0:not full
			output reg isEmptyMera, //1: empty, 0:not empty
			output reg unlockMera,	//1:door is open, 0:closed
			output reg restrictionWarnDigital,//1:show warning, 0:do not show warning
			output reg isFullDigital, //1:full, 0:not full
			output reg isEmptyDigital, //1: empty, 0:not empty
			output reg unlockDigital //1:door is open, 0:closed
	);
	 
	// You may declare your variables below	
	integer x;
	initial begin
			numOfStuInMera=0;
			numOfStuInDigital=0;
			restrictionWarnMera=0;
			isFullMera=0;
			isEmptyMera=1'b1;
			unlockMera=0;		
			restrictionWarnDigital=0;
			isFullDigital=0;
			isEmptyDigital=1'b1;
			unlockDigital=0;
 
			
	end
	//Modify the lines below to implement your design
	always @(posedge CLK) 
	begin
	x=0;
	if (mode==2'b01)
	begin
	x=0;
	x=smartCode[0]+smartCode[1]+smartCode[2]+smartCode[3]+smartCode[4];
	restrictionWarnDigital<=0;
	restrictionWarnMera<=0;
	unlockMera<=0;
	unlockDigital<=0;		
		if(lab==1) //mera
		begin
	
			if(numOfStuInMera<15)
			begin
			unlockMera<=1;
			isEmptyMera<=0;
			numOfStuInMera=numOfStuInMera+1;
			end
			
			else if(numOfStuInMera<30)
			begin
				if(x==0 | x==2 | x==4)
				begin
				unlockMera<=1;
				isEmptyMera<=0;
				numOfStuInMera=numOfStuInMera+1;
                if(numOfStuInMera==30)
    				begin
    				isFullMera=1;
    				end
				end
				else
				begin
				restrictionWarnMera <= 1;
				end
			end
		
		end
		if(lab==0) //digital
		begin
			if(numOfStuInDigital<15)
			begin
			unlockDigital<=1;
			isEmptyDigital<=0;
			numOfStuInDigital=numOfStuInDigital+1;
			end
			
			else if(numOfStuInDigital<30)
			begin
				if(x==1 | x==3 | x==5)
				    begin
    				unlockDigital<=1;
    				isEmptyDigital<=0;
    				numOfStuInDigital=numOfStuInDigital+1;
    				if(numOfStuInDigital==30)
    				begin
    				isFullDigital=1;
    				end
    				end
				else
				begin
				restrictionWarnDigital<=1;
				end	
			end
		
		end
	end //mode 01
	
	else if (mode==2'b00)
	begin
	unlockMera<=0;
	unlockDigital<=0;	
		if(lab==1&& isEmptyMera==0) //mera
		begin
		unlockMera<=1;
		numOfStuInMera = numOfStuInMera-1;
				if(numOfStuInDigital == 0)
					begin
					isEmptyDigital<=1;
					end
		end
		
		if(lab==0 && isEmptyDigital==0) // digital
		begin
		unlockDigital<=1;
		numOfStuInDigital = numOfStuInDigital-1;
				if(numOfStuInDigital==0)
					begin
					isEmptyDigital<=1;
					end
		end
	end //mode 00
	
	else
	begin 
	unlockDigital<=0;
	unlockMera<=0;
	restrictionWarnDigital <= 0;
	restrictionWarnMera <= 0;
	end
	
	end //alwaysblock
	
endmodule