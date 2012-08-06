plan {
     reach sentence_lengths,parts_time_summary via (Corpus: nist08_devtest nist08_dev) * (Algorithm: naive histogram words time)
}

global {
       ducttape_output="figures"
       algorithm=(Algorithm: naive histogram words time)
       split_script="../../ruby/balance-corpus"
       corpus=(Corpus: nist08_devtest="corpus/devtest.tok.norm.clean.ur" nist08_dev="corpus/dev.tok.norm.clean.ur")
       times=(Corpus: nist08_devtest="corpus/devtest.tok.norm.clean.ur.times" nist08_dev="corpus/dev.tok.norm.clean.ur.times")
       parts=(Parts: 64)
}

task sentence_lengths 
     < corpus=@
     > output="plot.tex"
{
	cat ${corpus} | ruby -e "

	    puts '\addplot';
	    puts 'coordinates{';
	   
	    n=0;
	    STDIN.each_line { |line|
	        n+=1;
		parts = line.strip.split
		puts \"(#{n},#{parts.size})\";		
	    }

	    puts '};'

	" > ${output}
}


task parts_time_summary
     < split_script=@
     < corpus=@
     < times=@
     > output="plot.tex"
    :: algorithm=@
    :: parts=@ 
{
	if [ "${algorithm}" == "time" ]; then
	    ${split_script} --balance-${algorithm} ${times} --corpus ${corpus} -n ${parts} -p ${algorithm}. --index-prefix ${algorithm}-index.
	else
	    ${split_script} --balance-${algorithm} --corpus ${corpus} -n ${parts} -p ${algorithm}. --index-prefix ${algorithm}-index.
	fi

	for f in ${algorithm}-index.*; do

		ruby -e "
		     times=Array.new
		     IO.foreach(ARGV[0]) { |time|
			times.push(time.strip.to_f)
		     }
		     
		     sum=0.0
		     IO.foreach(ARGV[1]) { |index|
	                 sum += times[index.to_i]
        	     }
	     
	             puts sum
		" ${times} ${f} > ${algorithm}-time-sum.${f##*.}

	done
	
	echo '\addplot' > ${output}
	echo 'coordinates{' >> ${output}
	cat ${algorithm}-time-sum.* | ruby -e '
	    n=0;
	    STDIN.each_line { |line|
	        n+=1;
		puts "(#{n},#{line.strip})"
	    }
	' >> ${output}
	echo '};' >> ${output}

}
