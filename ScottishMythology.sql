DROP SCHEMA IF EXISTS ScottishMythology;
CREATE SCHEMA ScottishMythology;
USE ScottishMythology;

DROP TABLE IF EXISTS BeingLocation;
DROP TABLE IF EXISTS BeingSymbolism;
DROP TABLE IF EXISTS BeingAbility;
DROP TABLE IF EXISTS BeingWeakness;
DROP TABLE IF EXISTS Being;
DROP TABLE IF EXISTS Location;
DROP TABLE IF EXISTS Story;
DROP TABLE IF EXISTS Ability;
DROP TABLE IF EXISTS Weakness;
DROP TABLE IF EXISTS Symbol;
DROP TABLE IF EXISTS Faction;
DROP TABLE IF EXISTS Gender;
DROP TABLE IF EXISTS Species;

-- Table housing species information, like "god", "bogle", "brownie"
CREATE TABLE Species(
	SpeciesPK int auto_increment primary key,
    SpeciesName varchar(20),
    SpeciesDescription varchar(255)
);
-- Table housing gender information, eg. "female", "male", "fluid", "unknown", "non-binary", "none"
CREATE TABLE Gender(
	GenderPK int auto_increment primary key,
    GenderType varchar(20)
);

-- Table housing faction information, eg. "Seelie Court", "Unseelie Court", "Spring/Summer", "Winter", "Neutral"
CREATE TABLE Faction(
	FactionPK int auto_increment primary key,
    FactionName varchar(100),
    FactionDescription varchar(255)
);

-- Table housing symbolism information, eg. "Love", "Spring", "Roses", "Winter", "Youth"
CREATE TABLE Symbol(
	SymbolPK int auto_increment primary key,
    SymbolName varchar(20)
);

-- Table housing weaknesses, eg. "Iron", "Pride", "Freshwater", "Salt", "Greed", "Lust"
CREATE TABLE Weakness(
	WeaknessPK int auto_increment primary key,
    WeaknessName varchar(20)
);

-- Table housing abilities, eg. "Hammer wielding", "arhcery", "warcraft", "invisibility"
CREATE TABLE Ability(
	AbilityPK int auto_increment primary key,
    AbilityName varchar(20)
);

-- Table housing story titles and descriptions
CREATE TABLE Story(
	StoryPK int auto_increment primary key,
    StoryTitle varchar(100),
    StoryDescription varchar(500)
);

-- Table housing locations
CREATE TABLE Location(
	LocationPK int auto_increment primary key,
    LocationName varchar(100),
    LocationDescription varchar(255)
);

-- Table housing individual characters, creatures, entities
CREATE TABLE Being(
	BeingPK int auto_increment primary key,
    BeingName varchar(100), 
    BeingSpecies int,
    BeingDescription varchar(255),
    BeingGender int,
    BeingArt BLOB, -- BLOB stores image data
    BeingFaction int,
    foreign key (BeingSpecies) references Species(SpeciesPK),
    foreign key (BeingGender) references Gender(GenderPK),
    foreign key (BeingFaction) references Faction(FactionPK)
);

-- Table matching Beings to the locations they are associated with
CREATE TABLE BeingLocation (
    BeingPK int,
    LocationPK int,
    primary key (BeingPK, LocationPK),
    foreign key (BeingPK) references Being(BeingPK),
    foreign key (LocationPK) references Location(LocationPK)
);

-- Table matching Beings to the Symbols they are associated with
CREATE TABLE BeingSymbolism(
	BeingPK INT,
    SymbolPK INT,
    primary key (BeingPK, SymbolPK),
    foreign key (BeingPK) references Being(BeingPK),
    foreign key (SymbolPK) references Symbol(SymbolPK)
);

-- Table matching Beings to Abilities
CREATE TABLE BeingAbility(
	BeingPK int,
    AbilityPk int,
    primary key (BeingPK, AbilityPK),
    foreign key (BeingPK) references Being(BeingPK),
    foreign key (AbilityPK) references Ability(AbilityPK)
);
-- Table matching Beings to Weaknesses
CREATE TABLE BeingWeakness(
	BeingPK int,
    WeaknessPk int,
    primary key (BeingPK, WeaknessPK),
    foreign key (BeingPK) references Being(BeingPK),
    foreign key (WeaknessPK) references Weakness(WeaknessPK)
);

-- Table matching Beings to Stories
CREATE TABLE BeingStory(
	BeingPK int,
    StoryPk int,
    primary key (BeingPK, StoryPK),
    foreign key (BeingPK) references Being(BeingPK),
    foreign key (StoryPK) references Story(StoryPK)
);

INSERT INTO Gender (GenderType)
VALUES
    ('Female'),
    ('Male'),
    ('Non-Binary'),
    ('Fluid'),
    ('None');
    
    INSERT INTO Faction (FactionName, FactionDescription)
VALUES
    ('Winter', 'Associated with The Cailleach, members of this faction are bent on creating and preserving The Enternal Winter'),
    ('Spring/Summer', 'Associated with Angus and Bride, members of this faction seek to restore the Spring and rescue Bride from the Cailleach\'s clutches.'),
    ('Seelie Court', 'A group of fairies with uncertain loyalties. A pecieved slight will result in vicious punishment, a good deed wonderful rewards... if they feel like it.'),
    ('Unseelie Court', 'A group of fairies who need no excuse to terrorise any who cross their path.'),
    ('Neutral', 'No particular affilations, may be open to persuation.');
    
    -- Inserting values related to The Cailleach so I have something to work with during development
    
    INSERT INTO Species(SpeciesName, SpeciesDescription)
VALUES
	('Diety', 'Exceptionally powerful fairy being with governance over an aspect of the world.');
    
	INSERT INTO Being (BeingName, BeingSpecies, BeingDescription, BeingGender, BeingFaction)
VALUES
    ('The Cailleach', 1, 'The one-eyed creator of Scotland. Blue skin, rust-red teeth, hair like snow, and goddess of winter and the wild.', 1, 1);
    
-- Inserting Cailleach related values into the locations.
INSERT INTO Location (LocationName, LocationDescription)
VALUES
    ('Corryvreckan', 'A whirlpool off the coast of Scotland which serves as The Cailleach\'s cauldron.'),
    ('Ben Nevis', 'The highest mountain in the British Isles, and throne of The Cailleach.');

-- Inserting Cailleach related values into the abilities
INSERT INTO Ability (AbilityName)
VALUES
    ('Ice'),
    ('Landscape Creation'),
    ('Hammers'),
    ('Staves'),
    ('Storm'),
    ('Shapeshifting');

-- Inserting Cailleach related values into the weaknesses
INSERT INTO Weakness (WeaknessName)
VALUES
    ('Insecurity'),
    ('Loneliness'),
    ('Heat'),
    ('Fire');

-- Inserting Cailleach related values into the symbols
INSERT INTO Symbol (SymbolName)
VALUES
    ('Winter'),
    ('Hammer'),
    ('Mountain'),
    ('Rebirth'),
    ('Wilderness'),
    ('Blue'),
    ('Deer'),
    ('Wolves'),
    ('Herons'),
    ('Eagles'),
    ('Ravens'),
    ('Blackthorn Trees');
    
    -- Insert BeingLocation junction data
INSERT INTO BeingLocation (BeingPK, LocationPK)
VALUES
    (1, 1), -- The Cailleach at Corryvreckan
    (1, 2); -- The Cailleach at Ben Nevis

-- Insert BeingAbility junction data
INSERT INTO BeingAbility (BeingPK, AbilityPK)
VALUES
    (1, 1), -- The Cailleach has the ability 'Ice'
    (1, 2), -- The Cailleach has the ability 'Landscape Creation'
    (1, 3), -- The Cailleach has the ability 'Hammers'
    (1, 4), -- The Cailleach has the ability 'Staves'
    (1, 5); -- The Cailleach has the ability 'Storm'

-- Insert BeingWeakness junction data
INSERT INTO BeingWeakness (BeingPK, WeaknessPK)
VALUES
    (1, 1), -- The Cailleach has the weakness 'Insecurity'
    (1, 2), -- The Cailleach has the weakness 'Loneliness'
    (1, 3), -- The Cailleach has the weakness 'Heat'
    (1, 4); -- The Cailleach has the weakness 'Fire'

-- Insert BeingSymbolism junction data
INSERT INTO BeingSymbolism (BeingPK, SymbolPK)
VALUES
    (1, 1), -- The Cailleach is associated with the symbol 'Winter'
    (1, 2), -- The Cailleach is associated with the symbol 'Hammer'
    (1, 3), -- The Cailleach is associated with the symbol 'Mountain'
    (1, 4), -- The Cailleach is associated with the symbol 'Rebirth'
    (1, 5), -- The Cailleach is associated with the symbol 'Wilderness'
    (1, 6), -- The Cailleach is associated with the symbol 'Blue'
    (1, 7), -- The Cailleach is associated with the symbol 'Deer'
    (1, 8), -- The Cailleach is associated with the symbol 'Wolves'
    (1, 9), -- The Cailleach is associated with the symbol 'Herons'
    (1, 10), -- The Cailleach is associated with the symbol 'Eagles'
    (1, 11), -- The Cailleach is associated with the symbol 'Ravens'
    (1, 12); -- The Cailleach is associated with the symbol 'Blackthorn Trees'
    

INSERT INTO Story (StoryTitle, StoryDescription)
VALUES
    ('Angus and Bride', 'In an attempt to secure an eternal winter, The Cailleach captures Bride, goddess of Spring and enslaves her at Ben Nevis. Angus, Bride\'s true love and the King of Summer, rescues her and together they defeat the Cailleach in a great battle. But the Cailleach can never truly die, and some say she and Bride are one in the same.');
INSERT INTO BeingStory (BeingPK, StoryPK)
VALUES
    (1, 1);
    