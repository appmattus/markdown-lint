# Heading

## Empty links

[text]() {NoEmptyLinksRule}

[text](<>) {NoEmptyLinksRule}

[text](#) {NoEmptyLinksRule}

[text][frag] {NoEmptyLinksRule}

[frag]: #

## Non-empty links

[text](link)

[text](link "title")

[text](<link>)

[text](#frag)

[text][ref]

[ref]: link

[text]

[text]: link
