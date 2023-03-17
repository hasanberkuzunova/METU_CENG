`timescale 1ns / 1ps

module ab(input A, input B, input clk, output reg Q);

    initial Q = 0;

    // You can implement your code here
    // ...
	always@(posedge clk)
	begin
		if (( A == 0 ) && ( B == 0 )) Q<=Q;
		if (( A == 1 ) && ( B == 1 )) Q<=~Q;
		if (( A == 0 ) && ( B == 1 )) Q<=1;
		if (( A == 1 ) && ( B == 0 )) Q<=0;
	end
endmodule
 
module ic1337(// Inputs
              input I0,
              input I1,
              input I2,
              input clk,
              // Outputs
              output Q0,
              output Q1,
              output Z);

    // You can implement your code here
    // ...
	
	wire a=~(I0 | ~I1) & ~I2;
	wire b=(~(~(~I1 | I2) ^ I0));
	ab ab_1 (a,I2,clk,Q0);
	ab ab_2 ((~I2),b,clk,Q1);	
   assign Z = Q0 ^ Q1;		
endmodule
