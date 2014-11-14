package com.freader.parser;


import java.util.ArrayList;
import java.util.regex.Matcher;

public class SimpleFb2Parser {

    public static EBook parse(String content){
        EBook eBook = new EBook();
        String source = createSource(content,eBook);
        parse(eBook,source);
        eBook.parsedBook = getBookBody(source);
        return eBook;
    }

    private static String createSource(String content,EBook eBook){
    	Matcher matcher = SOP.fb2Annotation.matcher(content);
        if (matcher.find()) {
            eBook.annotation = matcher.group(1);
            content = matcher.replaceFirst("");
        }
        return content;
    }

    private static Person extractPerson(String input) {
        Matcher matcher;
        Person person = new Person();
        matcher = SOP.fb2FirstName.matcher(input);
        if (matcher.find())
            person.firstName = matcher.group(1).trim();
        matcher = SOP.fb2MiddleName.matcher(input);
        if (matcher.find())
            person.middleName = matcher.group(1).trim();
        matcher = SOP.fb2LastName.matcher(input);
        if (matcher.find())
            person.lastName = matcher.group(1).trim();
        return person;
    }

    private static void parse(EBook eBook,String source) {
        Matcher matcher;
        matcher = SOP.fb2Author.matcher(source);
        while (matcher.find())
            eBook.authors.add(extractPerson(matcher.group(1)));
        matcher = SOP.fb2BookTitle.matcher(source);
        if (matcher.find())
            eBook.title = matcher.group(1);
        matcher = SOP.fb2genre.matcher(source);
        while (matcher.find())
            eBook.fb2Genres.add(matcher.group(1));
        matcher = SOP.fb2Language.matcher(source);
        if (matcher.find())
            eBook.language = matcher.group(1);
        matcher = SOP.fb2Sequence.matcher(source);
        if (matcher.find()) {
            String sequence = matcher.group(1);
            matcher = SOP.fb2SequenceName.matcher(sequence);
            if (matcher.find())
                eBook.sequenceName = matcher.group(1);
            matcher = SOP.fb2SequenceNumber.matcher(sequence);
            if (matcher.find())
                eBook.sequenceNumber = matcher.group(1);
        }
        eBook.isOk = true;
    }

    @SuppressWarnings("finally")
    private static ArrayList<String> getBookBody(String source) {
        Matcher body = SOP.fb2Body.matcher(source);
        ArrayList<String> col = new ArrayList<String>();
        try {
            if (body.find()) {
                Matcher title = SOP.fb2Title.matcher(body.group(0));
                Matcher paragraph;
                if (title.find()) {
                    paragraph = SOP.fb2Paragraph.matcher(title.group(0));
                    while (paragraph.find()) {
                        col.add(" %title "
                                + paragraph.group(0).replaceAll("<.*?>", ""));
                    }
                }
                Matcher epigraph = SOP.fb2PreBodyEpigraphs
                        .matcher(body.group());
                if (epigraph.find()) {
                    paragraph = SOP.fb2Paragraph.matcher(epigraph.group(0));
                    while (paragraph.find()) {
                        col.add(paragraph.group(0).replaceAll("<.*?>", ""));
                    }
                }
                Matcher section = SOP.fb2Section.matcher(body.group(0));
                while (section.find()) {
                    boolean newSection = true;
                    short paragraphIndexer = 0;
                    title = SOP.fb2Title.matcher(section.group(0));
                    StringBuilder s = new StringBuilder();
                    if (title.find()) {
                        if (newSection) {
                            s.append(" %new-section ");
                            newSection = false;
                        }
                        paragraph = SOP.fb2Paragraph.matcher(title.group(0));
                        while (paragraph.find()) {
                            s.append(" %title ");
                            s.append(paragraph.group(0).replaceAll("<.*?>", ""));
                            col.add(s.toString());
                            paragraphIndexer++;
                        }
                    }
                    paragraph = SOP.fb2Paragraph.matcher(section.group(0));
                    while (paragraphIndexer > 0) {
                        if (paragraph.find()) {
                            paragraph.group(0);
                            paragraphIndexer--;
                        }
                    }
                    while (paragraph.find()) {
                        col.add(paragraph.group(0).replaceAll("<.*?>", ""));
                    }

                }
            }
        } catch (NullPointerException e) {
        } finally {
            return col;
        }
    }

}
