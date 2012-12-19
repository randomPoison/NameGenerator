#NameGenerator

Code by David Legare

excaliburHisSheath@gmail.com

##Download

[Current Executable Jarfile](https://github.com/excaliburHisSheath/NameGenerator/raw/master/NameGenerator.jar)

To open just double click like you would with any executable file. You need to have java installed on your system to run it.

##Use

This utility reads in a list of names from a source file and can then generate names similar to that source list.

To use, select a .txt file to use as a source file. The file should be formatted to have each name separated by whitespace.

To generate a list of names, enter the number of names you want and click 'generate'. The program will only output unique names generated, so you may not get as many names out as you asked for depending on how robust your input is.

To save the list of generated names, enter a name for your output file and the program will output all names generated so far to a .txt file in the same directory as NameGenerator.jar.

The program defaults to only recognizing english alphabetic characters and will simply ignore any unrecognized character. You can add or remove characters by simply typing them into the prompt. You man add or remove more than one character at once.

### Input vs Output

[Example input and output](https://docs.google.com/spreadsheet/ccc?key=0AuEzVS4v3FaddENNS1dNT0ZiRThlajBRczE4ZHZwSXc)

The more robust your sample names are the better the resulting generated names will be. The program generates the words one character at a time based on the probability of any one character following the previous two, so it will only generate letter pairings that it has seen before.

What this means is that if your sample file had only 

+ cat
+ dog

The program would only output

+ cat
+ dog

because no letters overlap. However, even if your input were

+ mouse
+ dog

the program still wouldn't be able to generate any new names because, even though both words have an 'o' in common, the program takes the characters in pairs and there are no character pairs in common.

If you have too few sample names or your sample names are too simple then the program will not be able to generate a variety of names and the output will be limited. Try to have at least 15-20 sample names for optimal results.

The program will also produce many names that are too long, too short, or simply don't work. It is up to you to pick the names you like best from the list. If you want to refine your output generate a large list, clean out the bad names, and feed the output back into the program.

##Bugs and Suggestions

You can contact excaliburHisSheath@gmail.com with any bugs found and I will try to fix them. Alternately 
, if you have experience with Java and know how to fix the bug yourself, feel free to do so and make a pull request.

If you have suggestions for features to be added/refined feel free to let me know, and the same goes for making the contribution yourself.

##Licensing

The original concept and implementation is credited to the author of [Oh, The Huge Manatee!](http://ohthehugemanatee.net/2009/10/the-magical-word-o-matic-or-markov-text-analysis-for-fun-and-non-profit/)

This work is licensed under a [Creative Commons Attribution-ShareAlike 3.0 Unported License](http://creativecommons.org/licenses/by-sa/3.0/)
