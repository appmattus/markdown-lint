# Relative links

[This exists](relative-links.md)

[This also exists](./relative-links.md)

[This does not exist](./nothing-to-see-here.md) {ValidRelativeLinksRule}

[This is absolute](/so-should-not-trigger.md)

[This is a web url](www.example.com) {MissingLinkSchemeRule}

[This is a web url](https://www.example.com)

[This is an email without scheme](example@example.com) {MissingLinkSchemeRule}

[This is an email with scheme](mailto:example@example.com)
