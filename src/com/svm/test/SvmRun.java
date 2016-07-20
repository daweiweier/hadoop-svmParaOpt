package com.svm.test;

import java.io.ByteArrayOutputStream;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.MultipleInputs;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;


public class SvmRun {
	public static void main(String args[]) throws Exception
	{	
		double bestacc = 0;
		double bestGamma = 0;
		double bestC =0;
		long startTime=System.currentTimeMillis();   //��ȡ��ʼʱ��  
	    Configuration conf = new Configuration();
	    

		FSDataInputStream in = null;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		FileSystem fs = FileSystem.get(conf);
		Job job = new Job(conf,"job");
		job.setOutputKeyClass(LongWritable.class);
		job.setOutputValueClass(Text.class);
		job.setJarByClass(SvmRun.class);
		job.setMapperClass(SvmMapper.class);
		job.setReducerClass(SvmReducer.class);
		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);	
				
		FileInputFormat.addInputPath(job,new Path("hdfs://Master:9000/user/kh/input/Param.txt"));
		FileOutputFormat.setOutputPath(job,new Path("hdfs://Master:9000/user/kh/output"));
		job.waitForCompletion(true);
		

	    int n = 0;
		in = fs.open(new Path( "hdfs://Master:9000/user/kh/output/part-r-00000"));
		IOUtils.copyBytes(in, out, 65536, false);
		String  result = out.toString();
	    StringTokenizer itr = new StringTokenizer(result.toString());
	      while (itr.hasMoreTokens()) {
	  	    String[] param = itr.nextToken().split(",");
	    	  if(n%2 == 1){
	      Double g = Double.valueOf(param[0]);
	      Double c = Double.valueOf(param[1]);
	      Double acc = Double.valueOf(param[2]);
	   	if(acc > bestacc){
    		bestacc=acc;
    		bestGamma=g;
    		bestC=c;
    	}
	    	  }
	  	   	n++;
	      }

	      System.out.println("������ȷ�ʣ�"+bestacc+"����gamma��"+bestGamma+"����C:"+bestC);
	      
			long endTime=System.currentTimeMillis(); //��ȡ����ʱ��   
			System.out.println("����Ѱ��ʱ�䣺 "+(endTime-startTime)/1000+"s");

	}
}
