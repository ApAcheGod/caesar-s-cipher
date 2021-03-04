import org.w3c.dom.ls.LSOutput;

import javax.swing.*;
import java.awt.event.*;
import java.util.*;

public class cesar extends JDialog {
    private JPanel contentPane;
    private JButton cryptButton;
    private JButton decryptButton;
    private JScrollPane panel1;
    private JScrollPane panel2;
    private JTextField textField1;
    private JButton clearButton;
    private JButton hackButton;
    private JTextArea textArea1;
    private JTextArea textArea2;
    static int key = 0;

    final static String russianAlphabet = "абвгдежзийклмнопрстуфхцчшщъыьэюя";
    final static char[] russian = russianAlphabet.toCharArray();
    final static String englishAlphabet = "abcdefghjklmnopqrstuvwxyz";
    final static char[] english = englishAlphabet.toCharArray();

    static SortedMap<Character, Double> russianFrequency = new TreeMap<>();

    public static void initFrequency(SortedMap<Character, Double> map){
        map.put('а', 0.062);
        map.put('б', 0.014);
        map.put('в', 0.038);
        map.put('г', 0.013);
        map.put('д', 0.025);
        map.put('е', 0.072);
        map.put('ж', 0.007);
        map.put('з', 0.016);
        map.put('и', 0.062);
        map.put('й', 0.010);
        map.put('к', 0.028);
        map.put('л', 0.035);
        map.put('м', 0.026);
        map.put('н', 0.053);
        map.put('о', 0.090);
        map.put('п', 0.023);
        map.put('р', 0.040);
        map.put('с', 0.045);
        map.put('т', 0.053);
        map.put('у', 0.021);
        map.put('ф', 0.002);
        map.put('х', 0.009);
        map.put('ц', 0.003);
        map.put('ч', 0.012);
        map.put('ш', 0.006);
        map.put('щ', 0.003);
        map.put('ъ', 0.014);
        map.put('ы', 0.016);
        map.put('ь', 0.014);
        map.put('э', 0.003);
        map.put('ю', 0.006);
        map.put('я', 0.018);
    }


    public static String prepareString(String inputText){
        String outputText = inputText.toLowerCase(Locale.ROOT).replace("ё", "е").replace(" ", "");

        String taboo = " \n' 1234567890![]@#$%^&*()»,.«…_–-—+!№;%:?*/\\\"~";

        for(char c: taboo.toCharArray()){
            outputText = outputText.replace(c, ' ');
        }
        return outputText;
    }

    public static SortedMap<Character, Double> textFrequency(String inputText){
        SortedMap<Character, Double> textFrequency = new TreeMap<>();
        String cleanText = prepareString(inputText);
        char[] text = cleanText.toCharArray();
        for(int i = 0; i < text.length; i++){
            double countOfChar = 0;
            for(int j = 0; j < text.length; j++){
                if(text[i] == text[j]){
                    countOfChar++;
                }
            }
            textFrequency.put(text[i], (countOfChar / text.length));
        }

        for(int i = 0; i < russianAlphabet.length(); i++){
            if(!textFrequency.containsKey(russian[i])){
                textFrequency.put(russian[i], 0.0);
            }
        }

        return textFrequency;

    }

    public static int findKey(SortedMap<Character, Double> russianFrequency, Map<Character, Double> textFrequency){
        double minD = Double.MAX_VALUE;
        double dSum = 0;
        int minIter = 0;

        initFrequency(russianFrequency);

        ArrayList<Double> russianValues = new ArrayList<Double>(russianFrequency.values());
        ArrayList<Double> textValues = new ArrayList<Double>(textFrequency.values());

        for(int j = 0; j < russianValues.size(); j++){
            for(int i = 0; i < textValues.size(); i++){
                double d = Math.pow(russianValues.get(i) - textValues.get((i + j) % russianFrequency.size()), 2);
                dSum += d;
            }

            if(dSum < minD){
                minD = dSum;
                minIter = j;
            }
            dSum = 0;
        }
        return minIter;
    }

    public static String hack(String inputText){

        key = findKey(russianFrequency, textFrequency(prepareString(inputText)));
        return decrypt(inputText);
    }

    public static String encrypt(String InputText){

        char[] text  = InputText.toCharArray();
        ArrayList<Character> result = new ArrayList<>();
        for(char t: text){
            if(russianAlphabet.contains(Character.toString(t))){

                int oldIndex = russianAlphabet.indexOf(t);
                int delta = (oldIndex + key) % russianAlphabet.length();
                int newIndex = (russianAlphabet.length() + delta) % russianAlphabet.length();

                result.add(russian[newIndex]);

            }else if(englishAlphabet.contains(Character.toString(t))){

                int oldIndex = englishAlphabet.indexOf(t);
                int delta = (oldIndex + key) % englishAlphabet.length();
                int newIndex = (englishAlphabet.length() + delta) % englishAlphabet.length();

                result.add(english[newIndex]);
            }
        }

        StringBuilder outputText = new StringBuilder();
        for(int i = 0; i < result.size(); i++){
            if(i != 0 && i % 5 == 0){
                outputText.append(" ");
                outputText.append(result.get(i));
            }else{
                outputText.append(result.get(i));
            }
        }
        return outputText.toString();
    }


    public static String decrypt(String inputText){
        char[] text  = inputText.toCharArray();
        ArrayList<Character> result = new ArrayList<>();
        for(char t: text){
            if(russianAlphabet.contains(Character.toString(t))){

                int oldIndex = russianAlphabet.indexOf(t);
                int delta = (oldIndex - key) % russianAlphabet.length();
                int newIndex = (russianAlphabet.length() + delta) % russianAlphabet.length();

                result.add(russian[newIndex]);

            }else if(englishAlphabet.contains(Character.toString(t))){

                int oldIndex = englishAlphabet.indexOf(t);
                int delta = (oldIndex - key) % englishAlphabet.length();
                int newIndex = (englishAlphabet.length() + delta) % englishAlphabet.length();

                result.add(english[newIndex]);
            }
        }

        StringBuilder outputText = new StringBuilder();
        for(int i = 0; i < result.size(); i++){
            if(i != 0 && i % 5 == 0){
                outputText.append(" ");
                outputText.append(result.get(i));
            }else{
                outputText.append(result.get(i));
            }
        }
        return outputText.toString();
    }

    public cesar() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(cryptButton);

        cryptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onCrypt();
            }
        });

        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                onClear();
            }
        });

        decryptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onDecrypt();
            }
        });

        hackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onHack();
            }
        });

        setDefaultCloseOperation(HIDE_ON_CLOSE);

    }


    private void onClear(){
        textArea1.setText("");
        textArea2.setText("");
        textField1.setText("");
        key = 0;
    }

    private void onCrypt() {

        String inputText = textArea1.getText();
        try{
            key = Integer.parseInt(textField1.getText());
            textArea2.setText(encrypt(prepareString(inputText)));
        }catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Ключ может быть только целым числом!");
            textField1.setText("");
        }
    }


    private void onDecrypt() {

        String inputText = textArea1.getText();
        try{
            key = Integer.parseInt(textField1.getText());
            textArea2.setText(decrypt(prepareString(inputText)));
        }catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Ключ может быть только целым числом!");
            textField1.setText("");
        }
    }

    private void onHack(){
        String inputText = textArea1.getText();
        textArea2.setText(hack(prepareString(inputText)));
        textField1.setText(String.valueOf(key));
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (ClassNotFoundException |
                 InstantiationException |
                 IllegalAccessException |
                 UnsupportedLookAndFeelException e) {

        }
        cesar dialog = new cesar();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
