Obfuscated Messaging service


#Embed usage
F5/JpegEncoder for Java(tm)

Program usage: java Embed [Options] "InputImage"."ext" ["OutputFile"[.jpg]]

You have the following options:
-e <file to embed>	default: embed nothing
-p <password>		default: "abc123", only used when -e is specified
-q <quality 0 ... 100>	default: 80
-c <comment>		default: "JPEG Encoder Copyright 1998, James R. Weeks and BioElectroMech.  "

"InputImage" is the name of an existing image in the current directory.
  ("InputImage may specify a directory, too.) "ext" must be .tif, .gif,
  or .jpg.
  
Quality is an integer (0 to 100) that specifies how similar the compressed
  image is to "InputImage."  100 is almost exactly like "InputImage" and 0 is
  most dissimilar.  In most cases, 70 - 80 gives very good results.
  
"OutputFile" is an optional argument.  If "OutputFile" isn't specified, then
  the input file name is adopted.  This program will NOT write over an existing
  file.  If a directory is specified for the input image, then "OutputFile"
  will be written in that directory.  The extension ".jpg" may automatically be
  added.

Copyright 1998 BioElectroMech and James R. Weeks.  Portions copyright IJG and
  Florian Raemy, LCAV.  See license.txt for details.
Visit BioElectroMech at www.obrador.com.  Email James@obrador.com.
Steganography added by Andreas Westfeld, westfeld@inf.tu-dresden.de