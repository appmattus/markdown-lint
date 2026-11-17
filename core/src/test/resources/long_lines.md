This is a very very very very very very very very very very very very very very long line

[This long line is comprised entirely of a link](https://example.com "This is the long link's title")

[This long line is comprised entirely of a link](https://example.com "This is the long link's title").

[This long line is comprised entirely of a link](https://example.com "This is the long link's title")  

> [This long line is comprised entirely of a link](https://example.com "This is the long link's title")

    [This long line is comprised entirely of a link](https://example.com "But is inside a code block")

```markdown
[This long line is comprised entirely of a link](https://example.com "But is inside a code block")
```

This [long line is comprised mostly of a link](https://example.com "This is the long link's title")

[This long line is comprised mostly of a link](https://example.com "This is the long link's title") text

This long line includes a simple [reference][label] link and is long enough to violate the rule.

[This long line is comprised entirely of a reference link and is long enough to violate the rule][label]

[label]: https://example.org "Title for a link reference that is itself long enough to violate the rule"

[Link to broken label][notlabel]

[notlabel\]: notlink "Invalid syntax for a link label because the right bracket is backslash-escaped"

[](https://example.com "This long line is comprised entirely of a link with empty text and a non-empty title")

*[This long line is comprised of an emphasized link](https://example.com "This is the long link's title")*

_[This long line is comprised of an emphasized link](https://example.com "This is the long link's title")_

**[This long line is comprised of a bolded link](https://example.com "This is the long link's title")**

__[This long line is comprised of a bolded link](https://example.com "This is the long link's title")__

_**[This long line is comprised of an emphasized and bolded link](https://example.com "This is the long link's title")**_

**_[This long line is comprised of an emphasized and bolded link](https://example.com "This is the long link's title")_**

*[](https://example.com "This long line is comprised of an emphasized link with empty text and a non-empty title")*

**[](https://example.com "This long line is comprised of a bolded link with empty text and a non-empty title")**

{LineLengthRule:1} {LineLengthRule:11} {LineLengthRule:14}
{LineLengthRule:19} {LineLengthRule:21}
{ConsistentEmphasisStyleRule:35} {ConsistentEmphasisStyleRule:39}
{ConsistentEmphasisStyleRule:41} {ConsistentEmphasisStyleRule:43}

LineLengthRule:29 is invalid too but gets picked up as a link currently

This is a looongish [line](https://example.com "This is the long link's title")
This is a loooongish [line](https://example.com "This is the long link's title")
This is a looooongish [line](https://example.com "This is the long link's title")

This is a looongish [line](https://example.com "This is the long link's title")A
This is a loooongish [line](https://example.com "This is the long link's title")B

{LineLengthRule:61}

This is a long long long long long long long long long long long long loongish [line](https://example.com "This is the long link's title")
This is a long long long long long long long long long long long long looongish [line](https://example.com "This is the long link's title")
This is a long long long long long long long long long long long long loooongish [line](https://example.com "This is the long link's title")

{LineLengthRule:66} {LineLengthRule:67}

{NoReversedLinksRule:21} {NoReversedLinksRule:23} {NoReversedLinksRule:27}
{NoEmptyLinksRule:27}
