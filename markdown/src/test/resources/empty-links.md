# Heading

## Empty links

[text]() {NoEmptyLinksRule}

[text](<>) {NoEmptyLinksRule}

[text](#) {NoEmptyLinksRule}

[text][frag] {NoEmptyLinksRule}

[frag]: #

## Non-empty links

[text](link) {ValidRelativeLinksRule}

[text](link "title") {ValidRelativeLinksRule}

[text](<link>) {ValidRelativeLinksRule}

[text](#frag)

[text][ref]

[ref]: link

[text]

[text]: link

{ValidRelativeLinksRule:27} {ValidRelativeLinksRule:31}
