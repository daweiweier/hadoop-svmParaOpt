package com.svm.test;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class SvmReducer extends Reducer<LongWritable, Text, LongWritable, Text> {

	protected void reduce(LongWritable num, Iterable<Text> args,Context context)
			throws IOException, InterruptedException {
		double bestacc = 0;
		double bestGamma = 0;
		double bestC =0;
 		   for (Text val : args) {
			      String[] param = val.toString().split(",");
			      Double g = Double.valueOf(param[0]);
			      Double c = Double.valueOf(param[1]);
			      Double acc = Double.valueOf(param[2]);
			   	if(acc > bestacc){
		    		bestacc=acc;
		    		bestGamma=g;
		    		bestC=c;
		    	}
		      }
	        context.write(num,  new Text(bestGamma + "," + bestC + "," + bestacc));
	}

}
