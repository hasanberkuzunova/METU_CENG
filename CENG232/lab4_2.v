`timescale 1ns / 1ps

module lab4_2(// INPUTS
              input wire      mode,
              input wire[2:0] opCode,
              input wire[3:0] value,
              input clk,
              input reset,
              // OUTPUTS
              output reg[9:0] result,
              output reg cacheFull,
              output reg invalidOp,
              output reg overflow);
				  

    //================//
    // INITIAL BLOCK  //
    //================//
    //Modify the lines below to implement your design
		integer p0;
		integer p1;
		integer i;
		integer h;
	 	integer val;
	 	integer highcount;
	 	integer k;
	 	reg [6:0] memory[0:31];
		integer count;
		integer counter;
	 initial begin
		counter=0;
		p0=0;
		h=0;
		p1=0;
		count=0;
      highcount=0;
      i=0;
		val=0;
		result=0;
    end

    //================//
    //      LOGIC     //
    //================//
    //Modify the lines below to implement your design
    always @(posedge clk or posedge reset)
    begin
	 if(reset == 1)
	 begin
		p0=0;
		h=0;
		p1=0;
		count=0;
      highcount=0;
      i=0;
		val=0;
		counter=0;
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
		memory[16] = 0;
		memory[17] = 0;
		memory[18] = 0;
		memory[19] = 0;
		memory[20] = 0;
		memory[21] = 0;
		memory[22] = 0;
		memory[23] = 0;
		memory[24] = 0;
		memory[25] = 0;
		memory[26] = 0;
		memory[27] = 0;
		memory[28] = 0;
		memory[29] = 0;
		memory[30] = 0;
		memory[31] = 0;
	 end
	 else 
	 begin
		if(mode==0)//load
			begin
			if (opCode==3'b011 || opCode==3'b111 )
			begin
			invalidOp=1;
			end
			else
			begin
				if(i<=31)
					begin
					memory[i][6:4]=opCode;
					memory[i][3:0]=value;
					invalidOp=0;
					i=i+1;
					end
				else
					begin
					cacheFull=1;
					invalidOp=0;
					end
			 end
			end
			
			else if(mode==1)//calculate
			begin
				val=0;
				if(memory[count][6:4]==3'b000) //add
				begin
					val=memory[count][3:0];
					if(p0+val>1023)
						begin
						result=p0+val-1024;
						overflow=1;	
						end
					else
						begin
						result=p0+val;
						end	
				end
				else if(memory[count][6:4]==3'b001) //add2
					begin
					val=memory[count][3:0];
					if(p0+p1+val>1023)
						begin
						result=p0+p1+val-1024;
						overflow=1;	
						end
					else
						begin
						result=p0+p1+val;
						overflow=0;
						end
				end
			
			else if(memory[count][6:4]==3'b010) //fma
			begin
			val=memory[count][3:0];
				if(p0*p1+val>1023)
				begin
				result=p0*p1+val-1024;
				overflow=1;	
				end
				else
				begin
					result=p0*p1+val;
					overflow=0;
				end
			end
			
			else if(memory[count][6:4]==3'b100) //popc
			begin
			for(k=0;k<10;k=k+1)
			begin
				if(p0[k]==1)
				begin
					highcount=highcount+1;
				end
			end
			result=highcount;
			overflow=0;
			end
			
			else if(memory[count][6:4]==3'b101) //brev
			begin
				result[9]=p0[0];
				result[8]=p0[1];
				result[7]=p0[2];
				result[6]=p0[3];
				result[5]=p0[4];
				result[4]=p0[5];
				result[3]=p0[6];
				result[2]=p0[7];
				result[1]=p0[8];
				result[0]=p0[9];
				overflow = 0;
			end
			
			else if(memory[count][6:4]==3'b110) //setr
			begin
				val=memory[count][3:0];
				counter=val;
				overflow = 0;
			end
			
			p1=p0;
			p0=result;
			if(count == i+1)
			begin
				count=counter;
			end
			else
			begin
				count=count+1;
			end
			end
		end
	 end
endmodule
