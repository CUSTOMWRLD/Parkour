package com.customwrld.parkour.util;

import com.customwrld.customlib.CustomLib;
import com.customwrld.parkour.replays.Step;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

public class ReplayUtil {

    public static final Type TYPE = new TypeToken<List<Step>>(){}.getType();

    public static String serializeSteps(List<Step> steps) {
        try {
            var baos = new ByteArrayOutputStream();

            try (Writer writer = new OutputStreamWriter(new DeflaterOutputStream(baos))) {
                writer.write(CustomLib.getCustomLib()
                        .getGson()
                        .toJson(steps, TYPE));
            }

            return Base64.getEncoder().encodeToString(baos.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "[]";
    }

    public static List<Step> deserializeSteps(String serialized) {
        try (Reader reader = new InputStreamReader(
                new InflaterInputStream(
                        new ByteArrayInputStream(
                                Base64.getDecoder().decode(serialized))))) {

            var sw = new StringWriter();

            char[] cbuf = new char[1024];
            for (int len; (len = reader.read(cbuf)) > 0;) {
                sw.write(cbuf, 0, len);
            }

            return CustomLib.getCustomLib()
                    .getGson()
                    .fromJson(sw.toString(), TYPE);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

}
