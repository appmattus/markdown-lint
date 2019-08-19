# Relative images

![This exists](relative-links.md)

![This also exists](./relative-links.md)

![This does not exist](./nothing-to-see-here.md) {ValidRelativeImagesRule}

![This is absolute](/so-should-not-trigger.md)

![This is a web url](www.example.com) {MissingLinkSchemeRule}

![This is a web url](https://www.example.com)

![This is an email without scheme](example@example.com) {MissingLinkSchemeRule}

![This is an email with scheme](mailto:example@example.com)

![This exists][ref1]
![This also exists][ref2]
![This does not exist][ref3]
![This is absolute][ref4]
![This is a web url][ref5]

[ref1]: relative-links.md "This exists"
[ref2]: ./relative-links.md "This also exists"
[ref3]: ./nothing-to-see-here.md "This does not exist {ValidRelativeImagesRule}"
[ref4]: /so-should-not-trigger.md "This is absolute"
[ref5]: https://www.example.com "This is a web url"
