`timescale 1ns / 1ps
module lab4ROM (input [3:0] romAddr, output reg[4:0] romOutput);

/*Write your code here*/
always@(romAddr)
	begin

if(romAddr == 4'b0000)
	romOutput = 5'b00000;
else if(romAddr == 4'b0001)
	romOutput = 5'b00001;
else if(romAddr == 4'b0010)
	romOutput = 5'b00110;
else if(romAddr == 4'b0011)
	romOutput = 5'b00111;
else if(romAddr == 4'b0100)
	romOutput = 5'b01011;
else if(romAddr == 4'b0101)
	romOutput = 5'b01100;
else if(romAddr == 4'b0110)
	romOutput = 5'b01101;
else if(romAddr == 4'b0111)
	romOutput = 5'b01110;
else if(romAddr == 4'b1000)
	romOutput = 5'b11101;
else if(romAddr == 4'b1001)
	romOutput = 5'b11110;
else if(romAddr == 4'b1010)
	romOutput = 5'b11111;
else if(romAddr == 4'b1011)
	romOutput = 5'b10000;
else if(romAddr == 4'b1100)
	romOutput = 5'b10111;
else if(romAddr == 4'b1101)
	romOutput = 5'b11000;
else if(romAddr == 4'b1110)
	romOutput = 5'b11001;
else if(romAddr == 4'b1111)
	romOutput = 5'b11010;
	
end

endmodule
																							
module lab4RAM (
	input ramMode, //0:read, 1:write
	input [3:0] ramAddr, 
	input [4:0] ramInput,
	input  ramOp, //0:polynomial, 1:derivative
	input [1:0] ramArg,  //00:+1, 01:+2, 10:-1, 11:-2
	input CLK, 
	output reg [8:0] ramOutput);

/*Write your code here*/	
reg [8:0] memory[0:15] ;
	integer result;
	integer arg;
	initial begin
		memory[0] = 0;
		memory[1] = 0;
		memory[2] = 0;
		memory[3] = 0;
		memory[4] = 0;
		memory[5] = 0;
		memory[6] = 0;
		memory[7] = 0;
		memory[8] = 0;
		memory[9] = 0;
		memory[10] = 0;
		memory[11] = 0;
		memory[12] = 0;
		memory[13] = 0;
		memory[14] = 0;
		memory[15] = 0;
		ramOutput = 0;
	end
	
		
always@(posedge CLK)
begin
	if(ramMode==1)//write
		begin
		result = 0;		
				if(ramArg==2'b00)
					arg=1;
					
				else if(ramArg==2'b01)
					arg=2;
					
				else if(ramArg==2'b10)
					arg=-1;
							
				else if(ramArg==2'b11)
					arg=-2;
				
				if(ramOp==0) // poly
				begin
				
					if(ramInput[0]==0)
						result=result+1;
					else
						result=result-1;
					
					if(ramInput[1]==0)
						result=result+arg;
					else
						result=result-arg;
					
					if(ramInput[2]==0)
						result=result+(arg*arg);
					else
						result=result-(arg*arg);
					
					if(ramInput[3]==0)
						result=result+(arg*arg*arg);
					else
						result=result-(arg*arg*arg);
					
					if(ramInput[4]==0)
						result=result+(arg*arg*arg*arg);																					
					else
						result=result-(arg*arg*arg*arg);
					
					
					if(result>=0)
					begin
						memory[ramAddr]=result;
					end
					else
					begin
						result=256-result;
						memory[ramAddr]=result;				
					end
				end//poly
				
				else if(ramOp==1)//der
				begin
					if(ramInput[1]==0)
						result=result+1;
					else
						result=result-1;
					
					if(ramInput[2]==0)
						result=result+(2*arg);
					else
						result=result-(2*arg);
					
					if(ramInput[3]==0)
						result=result+(3*arg*arg);
					else
						result=result-(3*arg*arg);
					
					if(ramInput[4]==0)
						result=result+(4*arg*arg*arg);
					else
						result=result-(4*arg*arg*arg);
					
					if(result>=0)
					begin
						memory[ramAddr]=result;
					end
					else
					begin
						result=256-result;
						memory[ramAddr]=result;				
					end
				end	//der							
			end//write
		end //always
	
	always@(ramMode==1)
	begin	
	 	assign ramOutput = memory[ramAddr]; 
	end
			
endmodule


module polMEM(input mode, input [3:0] memAddr, input op, input [1:0] arg, input CLK, output wire [8:0] memOutput);

	/*Don't edit this module*/
	wire [4:0]  romOutput;

	lab4ROM RO(memAddr, romOutput);
	lab4RAM RA(mode, memAddr, romOutput, op, arg, CLK, memOutput);



endmodule

