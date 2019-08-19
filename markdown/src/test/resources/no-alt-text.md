# Images with and without alternate text

![Alternate text](image.jpg) {ValidRelativeImagesRule}

![](image.jpg) {NoAltTextRule} {ValidRelativeImagesRule}

![Alternate text](image.jpg "Title") {ValidRelativeImagesRule}

![](image.jpg "Title") {NoAltTextRule} {ValidRelativeImagesRule}

Image without alternate text ![](image.jpg) in a sentence. {NoAltTextRule}

Reference image with alternate text ![Alternate text][notitle]

Reference image without alternate text ![][notitle] {NoAltTextRule}

Reference image with alternate text and title ![Alternate text][title]

Reference image without alternate text and title ![][title] {NoAltTextRule}

Link to image with alternate text [![Alternate text](image.jpg)](image.jpg)

Link to image without alternate text [![](image.jpg)](image.jpg) {NoAltTextRule}

[notitle]: image.jpg
[title]: image.jpg "Title"

{ValidRelativeImagesRule:11}
{ValidRelativeLinksRule:21} {ValidRelativeImagesRule:21}
{ValidRelativeLinksRule:23} {ValidRelativeImagesRule:23}
{ValidRelativeImagesRule:25} {ValidRelativeImagesRule:26}
