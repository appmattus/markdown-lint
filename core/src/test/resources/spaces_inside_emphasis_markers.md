Line with *Normal emphasis*

Line with **Normal strong**

Line with _Normal emphasis_ {ConsistentEmphasisStyleRule}

Line with __Normal strong__ {ConsistentEmphasisStyleRule}

Broken * emphasis * with spaces in {NoSpaceInEmphasisRule}

Broken ** strong ** with spaces in {NoSpaceInEmphasisRule}

Broken _ emphasis _ with spaces in {NoSpaceInEmphasisRule}

Broken __ strong __ with spaces in {NoSpaceInEmphasisRule}

Mixed *ok emphasis* and * broken emphasis * {NoSpaceInEmphasisRule}

Mixed **ok strong** and ** broken strong ** {NoSpaceInEmphasisRule}

Mixed _ok emphasis_ and _ broken emphasis _ {NoSpaceInEmphasisRule}
{ConsistentEmphasisStyleRule:21}

Mixed __ok strong__ and __ broken strong __ {NoSpaceInEmphasisRule}
{ConsistentEmphasisStyleRule:24}

Mixed *ok emphasis* **ok strong** * broken emphasis * {NoSpaceInEmphasisRule}

Multiple * broken emphasis * _ broken emphasis _ {NoSpaceInEmphasisRule}

One-sided *broken emphasis * {NoSpaceInEmphasisRule}

One-sided * broken emphasis* {NoSpaceInEmphasisRule}

Don't _flag on _words with underscores before them.

The same goes for words* with asterisks* after them.

```text
/*
 *
 * Ignore code blocks
 *
 */
```

    /**
     *
     * Ignore * code * blocks
     *
     */

`/* Ignore * code * blocks */`
