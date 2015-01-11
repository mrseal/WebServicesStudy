package com.cf.study.webservice.restful.restlet;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class Adages {

    private static CopyOnWriteArrayList<Adage> adages;
    private static AtomicInteger id;

    static {
        final String[] aphorisms = { "1. What can be shown cannot be said.", "2. If a lion could talk, we could not understand him.",
                "3. Philosophy is a battle against the bewitchment of our intelligence by means of language.",
                "4. Ambition is the death of thought.", "5. The limits of my language mean the limits of my world." };
        adages = new CopyOnWriteArrayList<Adage>();
        id = new AtomicInteger();
        for (final String str : aphorisms) {
            add(str);
        }
    }

    public static String toPlain() {
        String retval = "";
        for (final Adage adage : adages) {
            retval += adage.toString() + "\n";
        }
        return retval;
    }

    public static CopyOnWriteArrayList<Adage> getAdages() {
        return adages;
    }

    public static Adage find(final int id) {
        Adage adage = null;
        for (final Adage a : adages) {
            if (a.getId() == id) {
                adage = a;
                break;
            }
        }
        return adage;
    }

    public static void add(final String words) {
        final int localId = id.incrementAndGet();
        final Adage adage = new Adage();
        adage.setWords(words);
        adage.setId(localId);
        adages.add(adage);
    }

}