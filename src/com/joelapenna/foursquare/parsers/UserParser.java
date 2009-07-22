/**
 * Copyright 2009 Joe LaPenna
 */

package com.joelapenna.foursquare.parsers;

import com.joelapenna.foursquare.Foursquare;
import com.joelapenna.foursquare.error.FoursquareError;
import com.joelapenna.foursquare.error.FoursquareParseException;
import com.joelapenna.foursquare.types.User;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Log;

import java.io.IOException;

/**
 * @author Joe LaPenna (joe@joelapenna.com)
 * @param <T>
 */
public class UserParser extends AbstractParser<User> {
    private static final String TAG = "UserParser";
    private static final boolean DEBUG = Foursquare.DEBUG;

    @Override
    public User parseInner(XmlPullParser parser) throws XmlPullParserException, IOException,
            FoursquareError, FoursquareParseException {
        User user = new User();
        int eventType = parser.getEventType();

        while (eventType != XmlPullParser.END_DOCUMENT) {
            switch (eventType) {
                case XmlPullParser.START_TAG:
                    if (DEBUG) Log.d(TAG, "Tag Name: " + String.valueOf(parser.getName()));

                    String name = parser.getName();
                    if ("error".equals(name)) {
                        throw new FoursquareError(parser.getText());
                    } else if ("user".equals(name)) {
                        parseUserTag(parser, user);
                        return user;
                    }

                default:
                    if (DEBUG) Log.d(TAG, "Unhandled Event");
            }
            eventType = parser.nextToken();
        }
        return null;
    }

    public void parseUserTag(XmlPullParser parser, User user) throws XmlPullParserException,
            IOException, FoursquareError, FoursquareParseException {
        assert parser.getName() == "user";
        if (DEBUG) Log.d(TAG, "parsing user stanza");

        while (parser.nextTag() != XmlPullParser.END_TAG) {
            if (DEBUG) Log.d(TAG, "Tag Name: " + String.valueOf(parser.getName()));

            String name = parser.getName();
            if ("badges".equals(name)) {
                user.setBadges(new BadgesParser(new BadgeParser()).parse(parser));

            } else if ("cityid".equals(name)) {
                user.setCityid(parser.nextText());

            } else if ("cityshortname".equals(name)) {
                user.setCityshortname(parser.nextText());

            } else if ("firstname".equals(name)) {
                user.setFirstname(parser.nextText());

            } else if ("gender".equals(name)) {
                user.setGender(parser.nextText());

            } else if ("id".equals(name)) {
                user.setId(parser.nextText());

            } else if ("lastname".equals(name)) {
                user.setLastname(parser.nextText());

            } else if ("sendtwitter".equals(name)) {
                user.setSendtwitter(parser.nextText().equals("1"));
            } else {
                // Consume something we don't understand.
                if (DEBUG) Log.d(TAG, "Found tag that we don't recognize: " + name);
                parser.nextText();
            }
        }
        parser.nextToken();
    }
}
