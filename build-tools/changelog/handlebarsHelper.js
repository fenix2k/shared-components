Handlebars.registerHelper('ifContainsBugfixCommits', function (commits, options) {
    const regex = new RegExp('.*?fix.*[^]*', 'i')
    const revertRegex = new RegExp('.*?revert.*[^]*', 'i');

    for (let i = 0; i < commits.length; i++) {
        if (regex.test(commits[i].message) && !revertRegex.test(commits[i].message)) {
            return options.fn(this);
        }
    }
    return options.inverse(this);
});

Handlebars.registerHelper('ifContainsFeatureCommits', function (commits, options) {
    const regex = new RegExp('.*?feat.*[^]*', 'i')
    const revertRegex = new RegExp('.*?revert.*[^]*', 'i');

    for (let i = 0; i < commits.length; i++) {
        if (regex.test(commits[i].message) && !revertRegex.test(commits[i].message)) {
            return options.fn(this);
        }
    }
    return options.inverse(this);
});

Handlebars.registerHelper('ifContainsRevertCommits', function (commits, options) {
    const regex = new RegExp('.*?revert.*[^]*', 'i')

    for (let i = 0; i < commits.length; i++) {
        if (regex.test(commits[i].message)) {
            return options.fn(this);
        }
    }
    return options.inverse(this);
});

Handlebars.registerHelper('ifContainsOtherCommits', function (commits, options) {
    const fix = new RegExp('.*?fix.*[^]*', 'i')
    const feat = new RegExp('.*?feat.*[^]*', 'i')
    const revert = new RegExp('.*?revert.*[^]*', 'i');
    const merge = new RegExp('.*?Merge .*[^]*', 'i');

    for (let i = 0; i < commits.length; i++) {
        if (!(fix.test(commits[i].message) || feat.test(commits[i].message) || revert.test(commits[i].message) || merge.test(commits[i].message))) {
            return options.fn(this);
        }
    }
    return options.inverse(this);
});

// посчитать кол-во коммитов, не считаем merge
Handlebars.registerHelper('commitsCountInTag', function (commits) {
    let i = 0;
    for (let j = 0; j < commits.length; j++) {
        if (!/.*?merge.*[^]*/i.test(commits[j].message)) {
            i += 1;
        }
    }
    return i.toString();
});

// фича коммит
Handlebars.registerHelper('ifFeature', function (message, options) {
    const regex = new RegExp('.*?feat.*[^]*', 'i');
    const revertRegex = new RegExp('.*?revert.*[^]*', 'i');
    const merge = new RegExp('.*?Merge .*[^]*', 'i');

    if (regex.test(message) && !revertRegex.test(message) && !merge.test(message)) {
        return options.fn(this);
    }
    return options.inverse(this);
});

// багфикс коммит
Handlebars.registerHelper('ifBugfix', function (message, options) {
    const regex = new RegExp('.*?fix.*[^]*', 'i');
    const revertRegex = new RegExp('.*?revert.*[^]*', 'i');
    const merge = new RegExp('.*?Merge .*[^]*', 'i');

    if (regex.test(message) && !revertRegex.test(message) && !merge.test(message)) {
        return options.fn(this);
    }
    return options.inverse(this);
});

// реверт коммит
Handlebars.registerHelper('ifRevert', function (message, options) {
    const regex = new RegExp('.*?revert.*[^]*', 'i');
    const merge = new RegExp('^Merge .*[^]*', 'i');

    if (regex.test(message) && !merge.test(message)) {
        return options.fn(this);
    }
    return options.inverse(this);
});

// коммит "прочее"
Handlebars.registerHelper('ifOtherCommit', function (message, options) {
    const fix = new RegExp('.*?fix.*[^]*', 'i');
    const feat = new RegExp('.*?feat.*[^]*', 'i');
    const revert = new RegExp('.*?revert.*[^]*', 'i');
    const merge = new RegExp('.*?Merge .*[^]*', 'i');


    if (!(fix.test(message) || feat.test(message) || revert.test(message) || merge.test(message))) {
        return options.fn(this);
    }

    return options.inverse(this);
});

// формат текста коммита
Handlebars.registerHelper('formatCommitMessage', function (message) {
    const preformatRegex = /[\r\n]+/gi;
    const regexWithScope = /\(([^)]*)\):([^]*)/;
    const regexWithoutScope = /^@?[a-zA-Z]+\s?[:]\s?([^]*)/;

    message = message.replace(preformatRegex, '\n\t- ');

    const matchWithScope = regexWithScope.exec(message);
    const matchWithoutScope = regexWithoutScope.exec(message);

    if (matchWithScope) {
        return '**' + matchWithScope[1].toString() + '**' + matchWithScope[2];
    } else if (matchWithoutScope) {
        return matchWithoutScope[1];
    }
    return message;
});
