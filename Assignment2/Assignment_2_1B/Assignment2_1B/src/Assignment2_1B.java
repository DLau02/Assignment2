//import java.io.BufferedReader;
//import java.io.File;
//import java.io.FileReader;
import java.io.IOException;
//import java.net.URI;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Comparator;
//import java.util.HashMap;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.Reducer.Context;
//import org.apache.hadoop.mapreduce.Mapper.Context;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
//import org.apache.hadoop.mapreduce.lib.input.MultipleInputs;
//import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

public class Assignment2_1B {

    public static class Map
            extends Mapper<LongWritable, Text, Text, FloatWritable>{

        private final static FloatWritable temp = new FloatWritable(1);
        private Text YearandCountry = new Text(); // type of output key

        public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String[] mydata = value.toString().split(",");
            if(mydata.length == 8 && key.get()>0) {
            	if(mydata[0].contentEquals("Asia")) {
            		YearandCountry.set(mydata[1] + ", " + mydata[6]);
	            	if(Float.parseFloat(mydata[7]) != -99) {
	            		temp.set(Float.parseFloat(mydata[7]));
	            		context.write(YearandCountry, temp);
	            	}
            	}
            }
        }
    }

    public static class Reduce
            extends Reducer<Text,FloatWritable,Text,FloatWritable> {

        private FloatWritable result = new FloatWritable();

        public void reduce(Text key, Iterable<FloatWritable> values, Context context) throws IOException, InterruptedException {
            float sum = 0; // initialize the sum for each keyword
            int n = 0;
            for (FloatWritable val : values) {
                sum += val.get();
                n++;
            }
            result.set((sum/n));
            context.write(key, result); // create a pair <keyword, number of occurences>
        }
    }


    // Driver program
    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
        // get all args
        if (otherArgs.length != 2) {
            System.err.println("Usage: Assignment2_1B <in> <out>");
            System.exit(2);
        }

        // create a job with name "wordcount"
        Job job = Job.getInstance(conf, "Average Temperature");
        job.setJarByClass(Assignment2_1B.class);
        job.setMapperClass(Map.class);
        job.setReducerClass(Reduce.class);

        // uncomment the following line to add the Combiner job.setCombinerClass(Reduce.class);

        // set output key type
        job.setOutputKeyClass(Text.class);
        // set output value type
        job.setOutputValueClass(FloatWritable.class);
        //set the HDFS path of the input data
        FileInputFormat.addInputPath(job, new Path(otherArgs[0]));
        // set the HDFS path for the output
        FileOutputFormat.setOutputPath(job, new Path(otherArgs[1]));
        //Wait till job completion
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}