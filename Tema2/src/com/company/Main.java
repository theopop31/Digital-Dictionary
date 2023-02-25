package com.company;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {

        ArrayList<Dictionary> dictionaries = new ArrayList<>(0);
        readFromFolder(dictionaries);
        Library library = new Library(dictionaries);
        Dictionary dict;

        // construim un nou obiect de tip cuvant;
        Word w = createWord();
        ArrayList<String> text;
        Definition def;


        // testare addWord
        System.out.println("-----> Test addWord 1 <-----");
        if (library.addWord(w, "ro")) {
            dict = library.searchDict("ro");
            System.out.println("Adaugarea a fost facuta cu succes!");
            if (dict.searchWord(w.getWord()) != null) {
                dict.searchWord(w.getWord()).printWord();
            }
        } else {
            System.out.println("Cuvantul exista deja in dictionar");
        }
        System.out.println("-----> Test addWord 2 <-----");
        if (library.addWord(w, "ro")) {
            dict = library.searchDict("ro");
            System.out.println("Adaugarea a fost facuta cu succes!");
            if (dict.searchWord(w.getWord()) != null) {
                dict.searchWord(w.getWord()).printWord();
            }
        } else {
            System.out.println("Cuvantul exista deja in dictionar!");
        }

        System.out.println();

        // testare removeWord
        System.out.println("-----> Test removeWord 1 <-----");
        dict = library.searchDict("ro");
        System.out.println("Numarul de cuvinte inainte de stergere: " +
                            dict.getWords().size());
        if (library.removeWord("merge", "ro")) {
            System.out.println("Stergerea a fost facuta cu succes!");
        } else {
            System.out.println("Cuvantul nu exista in dictionar!");
        }
        System.out.println("Numarul de cuvinte dupa stergere: " +
                dict.getWords().size());
        System.out.println("-----> Test removeWord 2 <-----");
        System.out.println("Numarul de cuvinte inainte de stergere: " +
                dict.getWords().size());
        if (library.removeWord("merge", "ro")) {
            System.out.println("Stergerea a fost facuta cu succes!");
        } else {
            System.out.println("Cuvantul nu exista in dictionar!");
        }
        System.out.println("Numarul de cuvinte dupa stergere: " +
                dict.getWords().size());

        System.out.println();

        // testare addDefinitionForWord

        // construim o noua definitie
        text = new ArrayList<>(0);
        text.add("mâţa-popii");
        text.add("aportor");
        def = new Definition("Dicționar de sinonime", "synonyms", 2002, text);

        System.out.println("-----> Test addDefinitionForWord 1 <-----");
        System.out.println("Numarul de definitii inainte de adaugare: " +
                dict.searchWord("câine").getDefinitions().size());
        if (library.addDefinitionForWord("câine", "ro", def)) {
            System.out.println("Adaugare reusita!");
        } else {
            System.out.println("Adaugarea a esuat!");
        }
        System.out.println("Numarul de definitii dupa adaugare: " +
                dict.searchWord("câine").getDefinitions().size());
        System.out.println("-----> Test addDefinitionForWord 2 <-----");
        System.out.println("Numarul de definitii inainte de adaugare: " +
                dict.searchWord("câine").getDefinitions().size());
        def = new Definition("Dicționar de sinonime", "synonyms", 2002, text);
        if (library.addDefinitionForWord("câine", "ro", def)) {
            System.out.println("Adaugare reusita!");
        } else {
            System.out.println("Adaugarea a esuat!");
        }
        System.out.println("Numarul de definitii dupa adaugare: " +
                dict.searchWord("câine").getDefinitions().size());

        System.out.println();

        // testare removeDefinition

        System.out.println("-----> Test removeDefinition 1 <-----");
        System.out.println("Numarul de definitii inainte de adaugare: " +
                dict.searchWord("câine").getDefinitions().size());
        if (library.removeDefinition("câine", "ro",
                "Dicționarul explicativ al limbii române (ediția a II-a revăzută și adăugită)")) {
            System.out.println("Stergerea a reusit!");
        } else {
            System.out.println("Stergerea a esuat!");
        }
        System.out.println("Numarul de definitii dupa adaugare: " +
                dict.searchWord("câine").getDefinitions().size());
        System.out.println("-----> Test removeDefinition 2 <-----");
        System.out.println("Numarul de definitii inainte de adaugare: " +
                dict.searchWord("câine").getDefinitions().size());
        if (library.removeDefinition("câine", "ro",
                "Dicționarul explicativ al limbii române (ediția a II-a revăzută și adăugită)")) {
            System.out.println("Stergerea a reusit!");
        } else {
            System.out.println("Stergerea a esuat!");
        }
        System.out.println("Numarul de definitii dupa adaugare: " +
                dict.searchWord("câine").getDefinitions().size());

        System.out.println();

        // testare translateWord
        System.out.println("-----> Test translateWord 1 <-----");
        String word = "chats";
        String translation = library.translateWord(word, "fr", "ro");
        if (translation != null && !translation.equals(word)) {
            System.out.println("Traducerea este: " + translation);
        } else {
            System.out.println("Nu exista traducere");
        }
        System.out.println("-----> Test translateWord 2 <-----");
        word = "manger";
        translation = library.translateWord(word, "fr", "ro");
        if (translation != null && !translation.equals(word)) {
            System.out.println("Traducerea este: " + translation);
        } else {
            System.out.println("Nu exista traducere");
        }

        System.out.println();

        // testare translateSentence
        System.out.println("-----> Testare translateSentence 1 <-----");
        String sentence = "chat jeu chats jeux";
        translation = library.translateSentence(sentence, "fr", "ro");
        if (translation != null) {
            System.out.println("Traducerea este: " + translation);
        }
        System.out.println("-----> Testare translateSentence 2 <-----");
        sentence = "pisici merg câine merge";
        translation = library.translateSentence(sentence, "ro", "fr");
        if (translation != null) {
            System.out.println("Traducerea este: " + translation);
        }

        // testare getDefinitionsForWord
        System.out.println("-----> Testare getDefinitionsForWord 1 <-----");
        ArrayList<Definition> definitions = library.getDefinitionsForWord("joc", "ro");
        if (definitions != null) {
            for (Definition d : definitions) {
                d.printDefinition();
            }
        }

        System.out.println("-----> Testare getDefinitionsForWord 2 <-----");
        definitions = library.getDefinitionsForWord("chec", "ro");
        if (definitions != null) {
            for (Definition d : definitions) {
                d.printDefinition();
            }
        }

        // testare exportDictionary
        System.out.println("-----> Testare exportDictionary 1 <-----");
        library.exportDictionary("ro");
        System.out.println("-----> Testare exportDictionary 2 <-----");
        library.exportDictionary("fr");
    }

    private static Word createWord() {
        ArrayList<String> singular = new ArrayList<>(0);
        singular.add("joc");
        ArrayList<String> plural = new ArrayList<>(0);
        plural.add("jocuri");
        ArrayList<Definition> definitions = new ArrayList<>(0);
        ArrayList<String> text = new ArrayList<>(0);
        text.add("delectare");
        text.add("distracţie");
        text.add("divertisment");
        Definition def = new Definition("Dicționar de sinonime", "synonyms", 2000, text);
        definitions.add(def);
        text = new ArrayList<>(0);
        text.add("Activitate distractivă (mai ales la copii)");
        text.add("Competiție sportivă de echipă căreia " +
                "îi este proprie și lupta sportivă (baschet, fotbal, rugbi etc.)");
        text.add("(Și în sintagma joc de noroc) = distracție cu cărți, cu zaruri etc. " +
                "care angajează de obicei sume de bani și care se desfășoară după anumite reguli " +
                "respectate de parteneri, câștigul fiind determinat de întâmplare sau de calcul.");
        def = new Definition("Dicționarul explicativ al limbii române (ediția a II-a revăzută și adăugită)",
                "definitions",
                2009, text);
        definitions.add(def);
        return new Word("joc", "game", "noun", singular, plural, definitions);
    }

    private static void readFromFolder(ArrayList<Dictionary> dictionaries) {

        String folderPathname = "F:\\Cursuri\\AN II\\POO\\IntelliJ\\Tema2\\input";
        File folder = new File(folderPathname);
        File[] files = folder.listFiles();

        if (files == null) {
            System.out.println("Folderul nu contine fisiere!");
            return;
        }
        // prelucrarea tuturor fisierelor din folder;
        for (File file : files) {
            // extragem limba din numele fisierului;
            String language = (file.getName().split("_"))[0];

            // construim un string care contine elementele in format JSON;
            StringBuilder jsonString = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = br.readLine()) != null) {
                    jsonString.append(line);

                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            // deserializam continutul fisierului intr-un arrayList de cuvinte;
            Type type = new TypeToken<ArrayList<Word>>() {}.getType();
            Gson gson = new Gson();
            ArrayList<Word> wordList = gson.fromJson(jsonString.toString(), type);

            // formam un nou dictionar pentru limba curenta si il adaugam la colectie;
            Dictionary dictionary = new Dictionary(language, wordList);
            dictionaries.add(dictionary);
        }
    }
}
