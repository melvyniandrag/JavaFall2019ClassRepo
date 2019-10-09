/*
 * Do this once for utf 8 and once for utf 16.
 *
 */

FileOutputStream fs = new FileOutputStream(file);
fs.write(str.toString().getBytes("UTF-16"));
fs.close();
