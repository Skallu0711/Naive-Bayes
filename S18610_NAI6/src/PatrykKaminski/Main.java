/**
 *
 * @Author : s18610
 *
 */

package PatrykKaminski;


import java.util.*;

public class Main {
    //|Outlook |Temp.|Humidity|Windy|Play?|
    public static String[][] learningData = {
            {"Sunny", "Hot", "High", "Weak", "No"},
            {"Sunny", "Hot", "High", "Strong", "No"},
            {"Overcast", "Hot", "High", "Weak", "No"},
            {"Rain", "Mild", "High", "Weak", "Yes"},
            {"Rain", "Cool", "Normal", "Weak", "Maybe"},
            {"Rain", "Cool", "Normal", "Strong", "Maybe"},
            {"Overcast", "Cool", "Normal", "Strong", "Maybe"},
            {"Sunny", "Mild", "High", "Weak", "No"},
            {"Sunny", "Cool", "Normal", "Weak", "Maybe"},
            {"Rain", "Mild", "Normal", "Weak", "Yes"},
            {"Sunny", "Mild", "Normal", "Strong", "Yes"},
            {"Overcast", "Mild", "High", "Strong", "Yes"},
            {"Overcast", "Hot", "Normal", "Weak", "Maybe"}
    };

    public static Map<String, Double> answers = countDecisionAtr();

    public static void main(String[] args) {
        String[] testingData = {"Overcast", "Hot", "Normal", "Weak"};

        System.out.println("Test data: " + Arrays.asList(testingData));
        System.out.println("Answer: " + findAnswer(testingData));

        System.out.println();
        System.out.println("a - add new data");
        System.out.println("q - end application");
        Scanner in = new Scanner(System.in);
        String s = "";
        String[] userInput = new String[learningData[0].length - 1];
        while (true) {
            s = in.nextLine();
            switch (s) {
                case "a":
                    for (int i = 0; i < learningData[0].length - 1; i++) {
                        System.out.println("Select one: ");
                        List<String> states = showPossibleAnswers(i);
                        for (int j = 0; j < states.size(); j++) {
                            System.out.println(j + ". " + states.get(j));
                        }
                        s = in.nextLine();
                        userInput[i] = states.get(Integer.parseInt(s));
                    }

                    System.out.println("Test data: " + Arrays.asList(userInput));
                    System.out.println("Answer: " + findAnswer(testingData));

                    System.out.println();
                    System.out.println("a - add new data");
                    System.out.println("q - end application");
                    break;
                case "q":
                    System.out.println("End");
                    System.exit(0);
                    break;
                default: {
                    System.out.println("There is no such command!");

                    System.out.println();
                    System.out.println("a - add new data");
                    System.out.println("q - end application");
                }
            }

        }

    }


    // NOWA METODA:
    // Liczy prawdopodobieństwo dla każdego atrybutu decyzyjnego i zwraca ten, dla którego jest najwyzsze

    private static String findAnswer(String[] testingData) {
        double bestValue = 0;
        String answer = "";
        for (Map.Entry<String, Double> entry : answers.entrySet()) {
            double value = naiveBayes(testingData, entry.getKey());
            if (bestValue < value) {
                bestValue = value;
                answer = entry.getKey();
            }
        }
        return answer;
    }

    public static double naiveBayes(String[] input, String decision) {
        double result = 1;

        for (int i = 0; i < input.length; i++) {
            result *= createFraction(input[i], i, decision); // przyklad: (overcast|yes) / ilosc yesow = 4/9
        }

        return result * (answers.get(decision) / learningData.length);
    }

    public static double createFraction(String input, int pointer, String decision) {
        double numerator = 0;
        double denominator = answers.get(decision);

        for (int i = 0; i < learningData.length; i++) {
            String[] line = learningData[i];
            if (line[pointer] == input && line[line.length - 1] == decision) {
                numerator++;
            }
        }
        if (numerator == 0) {
            return laplace(denominator, pointer);
        }
        return numerator / denominator;
    }

    public static double laplace(double denominator, int pointer) {
        List<String> possibleAnswers = showPossibleAnswers(pointer);
        double newDenominator = denominator + possibleAnswers.size();

        return 1 / newDenominator;
    }


    // PRZEROBIONA METODA:
    // Wczesniej liczyła yes i no
    // Teraz sprawdza ile jest atrybutów decyzyjnych i liczy każdy z osobna

    public static Map<String, Double> countDecisionAtr() {
        Map<String, Double> result = new HashMap<>();

        for (int i = 0; i < learningData.length; i++) {
            String[] line = learningData[i];
            String atr = line[line.length - 1];
            if (!result.containsKey(atr)) {
                result.put(atr, 1.0);
            } else {
                double lastValue = result.get(atr) + 1;
                result.replace(atr, lastValue);
            }
        }
        return result;
    }

    public static List<String> showPossibleAnswers(int pointer) {
        List<String> possibleAnswers = new ArrayList<>();
        for (int i = 0; i < learningData.length; i++) {
            if (!possibleAnswers.contains(learningData[i][pointer])) {
                possibleAnswers.add(learningData[i][pointer]);
            }
        }
        return possibleAnswers;
    }

}

