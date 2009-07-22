/**
 * Copyright 2008 Joe LaPenna
 */

package com.joelapenna.foursquared.widget;

import com.joelapenna.foursquare.types.Checkin;
import com.joelapenna.foursquare.types.Group;
import com.joelapenna.foursquare.types.Venue;
import com.joelapenna.foursquared.FoursquaredSettings;
import com.joelapenna.foursquared.R;
import com.joelapenna.foursquared.util.StringFormatters;

import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * @author Joe LaPenna (joe@joelapenna.com)
 */
public class CheckinListAdapter extends BaseCheckinAdapter {
    private static final String TAG = "CheckinListAdapter";
    private static final boolean DEBUG = FoursquaredSettings.DEBUG;

    private LayoutInflater mInflater;

    public CheckinListAdapter(Context context, Group checkins) {
        super(context, checkins);
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (DEBUG) Log.d(TAG, "getView() called for position: " + position);
        // A ViewHolder keeps references to children views to avoid unnecessary
        // calls to findViewById() on each row.
        ViewHolder holder;

        // When convertView is not null, we can reuse it directly, there is no
        // need to re-inflate it. We only inflate a new View when the
        // convertView supplied by ListView is null.
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.checkin_list_item, null);

            // Creates a ViewHolder and store references to the two children
            // views we want to bind data to.
            holder = new ViewHolder();
            holder.firstLine = (TextView)convertView.findViewById(R.id.firstLine);
            holder.shoutTextView = (TextView)convertView.findViewById(R.id.shoutTextView);
            holder.timeTextView = (TextView)convertView.findViewById(R.id.timeTextView);

            convertView.setTag(holder);
        } else {
            // Get the ViewHolder back to get fast access to the TextView
            // and the ImageView.
            holder = (ViewHolder)convertView.getTag();
        }

        Checkin checkin = (Checkin)getItem(position);
        holder.firstLine.setText(StringFormatters.getCheckinMessage(checkin));
        holder.timeTextView.setText(StringFormatters
                .getRelativeTimeSpanString(checkin.getCreated()));

        if (checkin.getShout() != null) {
            holder.shoutTextView.setText(checkin.getShout());
            holder.shoutTextView.setVisibility(TextView.VISIBLE);
        } else {
            holder.shoutTextView.setVisibility(TextView.GONE);
        }

        return convertView;
    }

    public static Venue venueFromCheckin(Checkin checkin) {
        Venue venue = new Venue();
        venue.setAddress(checkin.getVenue().getAddress());
        venue.setCity(checkin.getVenue().getCity());
        venue.setCrossstreet(checkin.getVenue().getCrossstreet());
        venue.setGeolat(checkin.getVenue().getGeolat());
        venue.setGeolong(checkin.getVenue().getGeolong());
        venue.setId(checkin.getVenue().getId());
        venue.setName(checkin.getVenue().getName());
        return venue;
    }

    private static class ViewHolder {
        TextView firstLine;
        TextView shoutTextView;
        TextView timeTextView;
    }
}
