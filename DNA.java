import java.util.*;
import java.io.*;

// Phil Lin
// 11/22/2018
// CSE142
// TA: Oliver Eriksen
// Assignment #7
//
// This program can read through a DNA file and produce a report of the information
// that the file contains.

public class DNA {
    public static final int MIN_NUM_OF_CODONS = 5;
    public static final int VALID_MASS_PERCENTAGE = 30;
    public static final int UNIQUE_NUCLEOTIDES = 4;
    public static final int NUC_PER_CODON = 3;

    public static void main(String[] args) throws FileNotFoundException {
        Scanner console = new Scanner(System.in);
        intro();
        System.out.print("Input file name? ");
        Scanner fileScanner = new Scanner(new File(console.next()));
        System.out.print("Output file name? ");
        PrintStream output = new PrintStream(new File(console.next()));

        while (fileScanner.hasNextLine()) {
            String regionName = fileScanner.nextLine();
            String sequence = fileScanner.nextLine();
            report(output, regionName, sequence);
        }
    }

    // The introduction of what this program does
    public static void intro() {
        System.out.println("This programs reports information about DNA");
        System.out.println("nucleotide sequences that may encode proteins.");
    }

    // convert "A, C, T, G" into "0, 1, 2, 3", respectively
    // the numbers are the index that those nucleotides should be in the arrays
    // pass the char type value "nucleotide" so we can check it
    // return a int type value "index"
    public static int letterToIndex(char nucleotide) {
        int index = 0;
        if (nucleotide == 'A') {
            index = 0;
        } else if (nucleotide == 'C') {
            index = 1;
        } else if (nucleotide == 'G') {
            index = 2;
        } else if (nucleotide == 'T') {
            index = 3;
        }
        return index;
    }
    // calculate the total mass of a sequence
    // pass String sequence so we can use use for loop to traver through it
    // return total mass as a double type value
    public static double totalMass(String sequence) {
        int[] countBoard = count(sequence);
        double[] masses = {135.128, 111.103, 151.128, 125.107};
        double junkMass = 0;
        double otherMass = 0;
        //traverse through the String sequence to find every dash and calculate the mass
        for (int i = 0; i < sequence.length(); i++) {
            if (sequence.charAt(i) == '-') {
                junkMass += 100;
            }
        }
        //using Array countBoard and masses to calculate the mass besides the junk
        for (int i = 0; i < UNIQUE_NUCLEOTIDES; i++) {
            otherMass += countBoard[i] * masses[i];
        }
        double totalMass = junkMass + otherMass;
        return totalMass;
    }

    // count the occurrences of the four nucleotides
    // pass the String sequence to get its information
    // return Array countBoard in which each element represents the total number of each
    // type of nucleotides in the sequence.
    public static int[] count(String sequence) {// need to be revised
        int[] countBoard = new int[UNIQUE_NUCLEOTIDES]; //A - 0, C - 1, G - 2, T - 3
        sequence = sequence.replace("-", "").toUpperCase();
        for (int i = 0; i <= sequence.length() - 1; i++) {
            // using method letterToIndex to convert every character in the sequence
            // into its corresponding index in the Array countBoard
            countBoard[letterToIndex(sequence.charAt(i))]++;
        }
        return countBoard;
    }

    //calculate the mass percentage of each nucleotide
    public static double[] massPercentage(String sequence) {
        int[] countBoard = count(sequence);
        double[] mPercent = new double[UNIQUE_NUCLEOTIDES];
        double[] masses = {135.128, 111.103, 151.128, 125.107};

        for (int i = 0; i <= UNIQUE_NUCLEOTIDES - 1; i++) {
            mPercent[i] = Math.round(countBoard[i] * masses[i] / totalMass(sequence)
                    * 1000.0) / 10.0;
        }
        return mPercent;
    }

    // to produce every group of codons in a DNA sequence
    // return an String array that contains every group of codons
    // pass the String sequence into the method so we can read through it
    public static String[] codonList(String sequence) {
        sequence = sequence.replace("-", "").toUpperCase();
        int size = sequence.length() / NUC_PER_CODON;
        String[] codonList = new String[size];
        for (int i = 0; i < size; i++) {
            codonList[i] = sequence.substring(NUC_PER_CODON *i, NUC_PER_CODON *(i+1));
        }
        return codonList;
    }

    // to test whether the DNA sequence is a protein
    // pass the String Array codonList and double array massPercentage because
    // we need the information they contain to decide whether the DNA is a protein
    public static String isProtein (String[]codonList, double[] massPercentage){
        String start = codonList[0];
        String end = codonList[codonList.length - 1];
        int size = codonList.length;
        double percent = massPercentage[1] + massPercentage[2];
        if (start.equals("ATG") && (end.equals("TAA") || end.equals("TAG") || end.equals("TGA"))
                && size >= MIN_NUM_OF_CODONS && percent >= VALID_MASS_PERCENTAGE) {
            return "YES";
        }
        return "NO";
    }

    //report - gather all the information and print them out in a PrintStream file
    //pass the PrintStream output to tell where the information should be printed out,
    //pass String regionName that will be gathered from the for loop in main method
    //pass String sequence because that is the most common parameters of all the methods
    //and also will be created in the main method.
    public static void report (PrintStream output, String regionName, String sequence){
        output.println("Region Name: " + regionName);
        output.println("Nucleotides: " + sequence.toUpperCase());
        output.println("Nuc. Counts: " + Arrays.toString(count(sequence)));
        output.println("Total Mass%: " + Arrays.toString(massPercentage(sequence)) + " of "
                + Math.round((totalMass(sequence)) * 10.0) / 10.0);
        output.println("Codons List: " + Arrays.toString(codonList(sequence)));
        output.println("Is Protein?: " + isProtein(codonList(sequence), massPercentage(sequence)));
        output.println();
    }
}