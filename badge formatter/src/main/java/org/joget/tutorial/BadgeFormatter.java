package org.joget.tutorial;

import java.util.HashMap;
import java.util.Map;
import org.joget.apps.app.service.AppPluginUtil;
import org.joget.apps.app.service.AppUtil;
import org.joget.apps.datalist.model.DataList;
import org.joget.apps.datalist.model.DataListColumn;
import org.joget.apps.datalist.model.DataListColumnFormatDefault;
import org.joget.commons.util.LogUtil;

public class BadgeFormatter extends DataListColumnFormatDefault{

   private final static String MESSAGE_PATH = "messages/BadgeFormatter";

   @Override
    public String getName() {
        return "Badge Datalist Formatter";
    }

   @Override
    public String getVersion() {
        return "8.0.0";
    }

   @Override
    public String getDescription() {
          return AppPluginUtil.getMessage("org.joget.tutorial.BadgeFormatter.pluginDesc", getClassName(), MESSAGE_PATH);
    }
    
   @Override
    public String getLabel() {
         return AppPluginUtil.getMessage("org.joget.tutorial.BadgeFormatter.pluginLabel", getClassName(), MESSAGE_PATH);
    }

   @Override
    public String getClassName() {
        return getClass().getName();
    }

   @Override
    public String getPropertyOptions() {
        return AppUtil.readPluginResource(getClassName(), "/properties/BadgeFormatter.json", null, true, MESSAGE_PATH);
    }
    
   @Override
    public String format(DataList dataList, DataListColumn column, Object row, Object value) {
        String result = (String) value;

        // Get the user-defined default badge type
        String defaultBadgeType = getPropertyString("defaultBadge");

        String badgeType = defaultBadgeType;

        if (result != null && !result.isEmpty()) {
            try {
                boolean isCaseSensitive = false;
                if (getPropertyString("statusCaseSensitivity") != null) {
                    isCaseSensitive = Boolean.parseBoolean(getPropertyString("statusCaseSensitivity"));
                }

                // set options
                Object[] options = (Object[]) getProperty("options");

                for (Object o : options) {
                    Map mapping = (HashMap) o;

                    // Check if the value matches a specific condition
                    boolean match = isCaseSensitive ? 
                                    ((String) value).equals((String) mapping.get("value")) :
                                    ((String) value).equalsIgnoreCase((String) mapping.get("value"));

                    if (match) {
                        badgeType = (String) mapping.get("badgeType");  // apply specific badge type
                        break;  // stop after the first match
                    }
                }

                result = "<span class='badge badge-" + badgeType + "' style='font-size: 14px;'>" + value + "</span>";

            } catch (Exception e) {
                LogUtil.error(getClassName(), e, "");
            }
        }
        return result;
}

}
