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
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
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
import org.apache.hadoop.mapreduce.lib.input.MultipleInputs;





public class Assignment2_1D {

    public static class MapTemp
            extends Mapper<LongWritable, Text, Text, Text>{

    	private Boolean first = true;
        private Text CityandTemp = new Text();
        private Text Country = new Text(); // type of output key

        public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String[] mydata = value.toString().split(",");
		    
		    if(first) {
		    	first = false;
		    	return;
		    }
		    else {
		    	Country.set(new Text(mydata[1]));
		        CityandTemp.set(new Text(mydata[3].trim() + "," + Float.valueOf(mydata[7].trim())));
		        context.write(Country, CityandTemp);
		    }
        }
    }
    
    public static class MapCap
	    extends Mapper<LongWritable, Text, Text, Text>{
		
    	private Boolean first = true;
		private Text Capital = new Text(); 
		private Text Country = new Text(); // type of output key
		
		public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
		    String[] mydata = value.toString().replaceAll("\"", "").split(",");
		    
		    if(first == true) {
		    	first = false;
		    	return;
		    }
		    else {
		    	Country.set(new Text(mydata[0].trim()));
			    Capital.set(new Text(mydata[1].trim()));
			    context.write(Country, Capital);
		    }
		}
	}

    public static class Reduce extends Reducer<Text, Text, Text, DoubleWritable> {
        private Text result = new Text(); //result will be a text 

        public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            int ctr = 0;
            double sum = 0.0, avg = 0.0, temp; 
            String capital="";
            String words[], city;
            

            for (Text v : values) {
                if (v.toString().indexOf(",") == -1) {
                	capital=v.toString().toLowerCase().trim();
                	break;
                }
            }
            //this loop will extract and process the temperature data to get the sum etc 
            for (Text v : values) {
         	   if (v.toString().indexOf(",") != -1) {
         		   words = v.toString().split(",");
         		   city = words[0];
         		   if (capital.isEmpty() == false) {
         			   temp = Double.valueOf(words[1]);
         			   sum+=temp;
         			   ctr++;
         		   }
         	   }
            }
            
            avg=sum/ctr;
            result.set(new Text(key + ", " + capital));
            
            
            if (!Double.isNaN(avg)) {
         	   context.write(result, new DoubleWritable(avg));
            }
            
        }
    }


    // Driver program
    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
        // get all args
        /*if (otherArgs.length != 2) {
            System.err.println("Usage: Assignment2_1D <in> <out>");
            System.exit(2);
        }*/

        // create a job with name "wordcount"
        Job job = Job.getInstance(conf, "Average Temperature");
        job.setJarByClass(Assignment2_1D.class);
        job.setReducerClass(Reduce.class);

        // uncomment the following line to add the Combiner job.setCombinerClass(Reduce.class);

        // set output key type
        job.setOutputKeyClass(Text.class);
        // set output value type
        job.setOutputValueClass(Text.class);
        //set the HDFS path of the input data
        MultipleInputs.addInputPath(job, new Path(otherArgs[0]), TextInputFormat.class, MapTemp.class);
        MultipleInputs.addInputPath(job, new Path(otherArgs[1]), TextInputFormat.class, MapCap.class);
        // set the HDFS path for the output
        FileOutputFormat.setOutputPath(job, new Path(otherArgs[2]));
        //Wait till job completion
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
