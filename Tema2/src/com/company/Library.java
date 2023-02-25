package com.company;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

public class Library {

    private ArrayList<Dictionary> dictionaries;

    public Library(ArrayList<Dictionary> dictionaries) {
        this.dictionaries = dictionaries;
    }

    Dictionary searchDict(String language) {
        /* metoda care returneaza dictionarul corespunzator limbii
        language sau null daca nu exista;
         */
        for (Dictionary dict : this.dictionaries) {
            if ((dict.getLanguage()).equals(language)) {
                return dict;
            }
        }
        return null;
    }

    boolean addWord(Word word, String language) {

        // cautam dictionarul corespunzator limbii dorite;
        Dictionary dictionary = searchDict(language);

        if (dictionary != null) {
            // parcurgem lista de cuvinte din dictionar;
            for (Word w : dictionary.getWords()) {
                // daca am gasit un cuvant cu acelasi nume;
                if ((w.getWord()).equals(word.getWord())) {
                    /* daca dictionarul contine deja exact cuvantul dat ca parametru
                    returnam false;
                     */
                    if (dictionary.getWords().contains(word)) {
                        return false;
                    } else {
                        /* cuvantul nu este identic, asa ca dorim sa vedem daca difera
                        definitiile (sau sinonimele);
                         */
                        ArrayList<Definition> def = w.getDefinitions();
                        for (int i = 0; i < def.size(); ++i) {
                            for (Definition definition : word.getDefinitions()) {
                                // daca dictionarul definitiei este identic verificam textul;
                                if ((def.get(i)).getDict().equals(definition.getDict())) {
                                    ArrayList<String> t = (def.get(i)).getText();
                                    for (String text : definition.getText()) {
                                        // adaugam textul daca nu exista deja;
                                        if (!t.contains(text)) {
                                            t.add(text);
                                        }
                                    }
                                } else {
                                    /* dictionarul definitiei nu este identic asa ca
                                    adaugam intreaga definitie;
                                     */
                                    if (!def.contains(definition)) {
                                        def.add(definition);
                                    }
                                }
                            }
                        }
                        return true;
                    }
                }
            }
            /* cuvantul nu exista deja in dictionar deci il vom
            adauga;
            */
            (dictionary.getWords()).add(word);
            return true;
        }
        System.out.println("Limba nu a fost gasita");
        return false;
    }

    boolean removeWord(String word, String language) {

        // cautam dictionarul corespunzator limbii dorite;
        Dictionary dictionary = searchDict(language);

        if (dictionary != null) {
            // parcurgem lista de cuvinte a dictionarului;
            for (Word w : dictionary.getWords()) {
                /* cautam cuvantul atat in formele de singular cat si in cele
                de plural, iar daca il gasim stergem obiectul corespunzator din
                dictionar;
                 */
                for (String s : w.getSingular()) {
                    if (word.equals(s)) {
                        dictionary.getWords().remove(w);
                        return true;
                    }
                }
                for (String s : w.getPlural()) {
                    if (word.equals(s)) {
                        dictionary.getWords().remove(w);
                        return true;
                    }
                }
            }
            // nu am gasit cuvantul deci returnam false;
            return false;
        }
        System.out.println("Limba nu a fost gasita");
        return false;
    }

    boolean addDefinitionForWord(String word, String language, Definition definition) {

        // cautam dictionarul corespunzator limbii dorite;
        Dictionary dictionary = searchDict(language);
        boolean ok;

        if (dictionary != null) {
            // cautam cuvantul in lista;
            for (Word w : dictionary.getWords()) {
                if (word.equals(w.getWord())) {
                    // cautam definitia;
                    for (Definition def : w.getDefinitions()) {
                        /* daca gasim o definitie cu nume, tip si an identic,
                        verificam si campul de text;
                         */
                            if ((def.getDict()).equals(definition.getDict()) &&
                                    def.getDictType().equals(definition.getDictType()) &&
                                    def.getYear() == definition.getYear()) {
                                /* verificam daca si sinonimele sau definitiile sunt identice
                                caz in care returnam false;
                                 */
                                ok = false;
                                for (String t : definition.getText()) {
                                    if (!def.getText().contains(t)) {
                                        def.getText().add(t);
                                        // marcam ca s-a facut minim o modificare;
                                        ok = true;
                                    }
                                }

                                if (!ok) {
                                    // definitia exista identica deja;
                                    System.out.println("Definitia exista deja!");
                                    return false;
                                } else {
                                    // am adaugat cel putin o definitie sau un sinonim
                                    System.out.println("Definitie actualizata cu succes!");
                                    return true;
                                }
                            }
                    }
                    /* definitia nu exista deloc sau exista dar are alt an de
                    publicare alt tip sau alt nume;
                     */
                    w.getDefinitions().add(definition);
                    System.out.println("Definitie adaugata cu succes!");
                    return true;
                }
            }
            System.out.println("Cuvantul nu a fost gasit");
            return false;
        }
        System.out.println("Limba nu a fost gasita");
        return false;
    }

    boolean removeDefinition(String word, String language, String dictionary) {

        // cautam dictionarul corespunzator limbii dorite;
        Dictionary dict = searchDict(language);
        boolean ok = false;
        int i;

        if (dict != null) {
            // cautam cuvantul in lista de cuvinte a dictionarului;
            for (Word w : dict.getWords()) {
                if (word.equals(w.getWord())) {
                    // cautam numele dictionarului in lista de definitii a cuvantului;
                    for (i = 0; i < w.getDefinitions().size(); ++i) {
                        if (dictionary.equals(w.getDefinitions().get(i).getDict())) {
                            // stergem definitia
                            w.getDefinitions().remove(w.getDefinitions().get(i));
                            // marcam ca am gasit cel putin o definitie de sters;
                            ok = true;
                            --i;
                        }
                    }
                    // daca definitia nu a fost gasita se va returna false;
                    return ok;
                }
            }
            System.out.println("Cuvantul nu a fost gasit");
            return false;
        }
        System.out.println("Limba nu a fost gasita");
        return false;
    }

    String translateWord(String word, String fromLanguage, String toLanguage) {

        String wordEn = null;
        int index = -1;
        String type = null;

        // cautam dictionarele corespunzetoare fiecarei limbi;
        Dictionary fromDictionary = searchDict(fromLanguage);
        Dictionary toDictionary = searchDict(toLanguage);


        if (fromDictionary != null) {
            /* cautam cuvantul de tradus si retinem varianta in engleza, tipul
            (daca e singular sau plural), si indicele pozitiei din lista de singular
            sau plural in cazul in care sunt mai multe forme sau cuvantul este un verb;
             */
            for (Word w : fromDictionary.getWords()) {
                if (word.equals(w.getWord())) {
                    index = 1;
                    type = "Infinitiv";
                    wordEn = w.getWord_en();
                }
                if (index != -1) {
                    break;
                }
                for (int i = 0; i < w.getSingular().size(); ++i) {
                    if (word.equals(w.getSingular().get(i))) {
                        index = i;
                        type = "Singular";
                        wordEn = w.getWord_en();
                    }
                }
                /* daca am gasit deja cuvantul in lista de singular ne oprim,
                altfel verificam si lista de plural;
                 */
                if (index != -1) {
                    break;
                }
                for (int i = 0; i < w.getPlural().size(); ++i) {
                    if (word.equals(w.getPlural().get(i))) {
                        index = i;
                        type = "Plural";
                        wordEn = w.getWord_en();
                    }
                }
                // daca am gasit cuvantul ne oprim din cautare;
                if (index != -1) {
                    break;
                }
            }
            /* daca am gasit cuvantul de tradus, cautam varianta in engleza in
            dictionarul limbii in care vrem sa traducem(daca exista) si returnam
            varianta corespunzatoare tipului si indicelui;
             */
            if (wordEn != null) {
                if (toDictionary != null) {
                    for (Word w : toDictionary.getWords()) {
                        if (wordEn.equals(w.getWord_en())) {
                            if (type.equals("Singular")) {
                                return w.getSingular().get(index);
                            } else if (type.equals("Plural")) {
                                return w.getPlural().get(index);
                            } else {
                                return w.getWord();
                            }
                        }
                    }
                    return word;
                }
                System.out.println("Nu exista dictionar pentru limba in care se traduce");
                return null;
            }
            System.out.println("Nu exista cuvantul de tradus");
            return null;
        }
        System.out.println("Nu exista dictionar pentru limba din care se traduce");
        return null;
    }

    String translateSentence(String sentence, String fromLanguage, String toLanguage) {
        /* Conform unei postari de pe forum, se ignora majusculele si semnele de punctuatie
        asa ca pentru input(propozitie) o sa consider un format in care toate cuvintele sunt
        scrise cu litere mici si despartite printr-un spatiu, fara semne de punctuatie.
         */
        String[] words = sentence.split(" ");
        StringBuilder translation = new StringBuilder();

        /* parcurgem propozitia cuvant cu cuvant si folosim metoda translateWord
        pentru fiecare cuvant, iar dupa formam din nou propozitia
         */

        for (String w : words) {
            translation.append(translateWord(w, fromLanguage, toLanguage));
            translation.append(" ");
        }

        return translation.toString();
    }

    ArrayList<String> translateSentences(String sentence, String fromLanguage, String toLanguage) {
        /* propozitia contine numai forme de singular */

        ArrayList<String> translations = new ArrayList<>(0);
        // cele 3 variante de traducere o contin si pe cea de la task-ul anterior
        translations.add(translateSentence(sentence, fromLanguage, toLanguage));

        String[] words = sentence.split(" ");
        Dictionary fromDictionary = searchDict(fromLanguage);
        Dictionary toDictionary = searchDict(toLanguage);

        // mai vrem sa incercam sa obtinem 2 variante de traducere
        for (int i = 0; i < 2; ++i) {
            String sen = "";
            // parcurgem cuvintele din propozitie
            for (String s : words) {
                String wordEn = null;
                // obtinem varianta in engleza a cuvantului
                if (fromDictionary != null) {
                    for (Word w : fromDictionary.getWords()) {
                        if (s.equals(w.getWord())) {
                            wordEn = w.getWord_en();
                            break;
                        }
                    }
                }

                if (wordEn != null) {
                    // cautam cuvantul in dictionarul limbii in care traducem
                    for (Word w : toDictionary.getWords()) {
                        if (wordEn.equals(w.getWord_en())) {
                            int ok = 0;
                            // cautam macar o definitie care contine sinonime
                            for (Definition def : w.getDefinitions()) {
                                if (def.getDictType().equals("synonyms")) {
                                    // daca numarul de sinonime este suficient il adaugam la propozitie
                                    if (def.getText().size() > i) {
                                        sen += def.getText().get(i);
                                        // marcam faptul ca am gasit sinonim
                                        ok = 1;
                                        break;
                                    }
                                }
                            }
                            /* daca pentru cel putin un cuvant nu am gasit sinonim
                            atunci nu vom adauga nimic la arrayList(facem propozitia
                            goala/vida)
                             */
                            if (ok == 0) {
                                sen = "";
                                break;
                            }
                        }
                    }
                    if (sen.equals("")) {
                        break;
                    }
                }
            }
            /* daca propozitia construita nu este goala(vida), atunci sigur contine
            o alta varianta de traducere, deci o adaugam
             */
            if (!sen.equals("")) {
                translations.add(sen);
            }
        }
        return translations;
    }

    ArrayList<Definition> getDefinitionsForWord(String word, String language) {
        /* presupunem ca pentru aceasta metoda forma cuvantului este cea de
        baza, deci parametrul word nu va fi o alta forma de singular sau plural;
         */
        ArrayList<Definition> definitionsForWord;

        // cautam dictionarul corespunzator limbii dorite;
        Dictionary dictionary = searchDict(language);

        if (dictionary != null) {
            // cautam cuvantul in dictionar;
            for (Word w : dictionary.getWords()) {
                if (word.equals(w.getWord())) {
                    // extragem lista de definitii
                    definitionsForWord = w.getDefinitions();
                    /* sortam si returnam lista(clasa Definition va implementa
                    Comparable si vom suprascrie metoda compareTo() pentru a
                    sorta elementele dupa an in ordine crescatoare
                     */
                    Collections.sort(definitionsForWord);
                    return definitionsForWord;
                }
            }
            System.out.println("Cuvantul nu exista in dictionar");
            return null;
        }
        System.out.println("Limba nu exista");
        return null;
    }

    void exportDictionary(String language) {

        // cautam dictionarul corespunzator limbii;
        Dictionary dictionary = searchDict(language);

        /* instantiem un nou obiect de tip gson caruia ii setam PrettyPrinting
        pentru a fi mai usor de citit obiectele json;
         */
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        // construim path-ul fisierului in care vrem sa scriem;
        String fileName = "F:\\Cursuri\\AN II\\POO\\IntelliJ\\Tema2\\output\\out_" + language + "_dict.json";
        // construim obiectul json si il scriem in fisier;
        try {
            Writer writer = new FileWriter(fileName);
            gson.toJson(dictionary.getWords(), writer);
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
