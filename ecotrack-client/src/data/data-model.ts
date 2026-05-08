// Session - login

export interface SessionData {
    username: string;
    message: string;
}

export interface ServerLoginResponse {
    sessionData: SessionData;
    user: UserData;
}

// Object

export class UserData {
    iduser: number;
    username: string;
    name: string;
    surname?: string;
    email: string;
    password: string;
    age: number;
    cover?: string;
    level?: number;
    xp?: number;
    groups: UserInGroupData[] = [];
    events: UserInEventData[] = [];
    ecologicalChallenges: UserInEcologicalChallengeData[] = [];
    tasks: UserInEventActivityData[] = [];
    badges: BadgeToUserData[] = [];
    obtainedTrophiesByGroups: TrophyToGroupData[] = [];

    constructor(idUser: number, username: string, name: string, email: string, password: string, age: number,
                surname?: string, cover?: string, xp?: number, level?: number) {
        this.iduser = idUser;
        this.username = username;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
        this.age = age;
        this.cover = cover;
        this.level = level;
        this.xp = xp;
    }

    static fromServerResponse(data: ServerLoginResponse): UserData {
        return new UserData(data.user.iduser, data.user.username, data.user.name, data.user.email, data.user.password,
            data.user.age, data.user.surname, data.user.cover, data.user.xp, data.user.level);
    }
}

export interface UserInGroupData {
    idusergroup: number;
    role: string;
    user: UserData;
    group: GroupData;
}

export interface GroupData {
    idgroup: number;
    type: string;
    name: string;
    description?: string;
    level?: number;
    xp?: number;
    cover?: string;
    organizedEvents?: EventData[];
    memberUsers?: UserInGroupData[];
    obtainedTrophies?: TrophyToGroupData[];
}

export interface UserInEventData {
    iduserevent: number;
    participationDate: Date;
    user: UserData;
    event: EventData;
}

export interface EventData {
    idevent?: number;
    name: string;
    description?: string;
    startDate: Date;
    endDate?: Date;
    category: string;
    state?: string;
    cover?: string;
    xp: number;
    place: string;
    targetCount?: number;
    hasActivity?: boolean;
    organizer: GroupData;
    attendees?: UserInEventData[];
    eventActivity?: EventActivityData[];
}

export interface UserInEcologicalChallengeData {
    id: number;
    state: string;
    updateDate: Date;
    user: UserData;
    ecologicalChallenge: EcologicalChallengeData;
}

export interface EcologicalChallengeData {
    idecochallenge: number;
    name: string;
    description?: string;
    category: string;
    xp: number;
    difficulty: number;
    cover?: string;
    participants?: UserInEcologicalChallengeData[];
}

export interface UserInEventActivityData {
    idusereventactivity: number;
    participationDate: Date;
    user: UserData;
    eventActivity: EventActivityData;
}

export interface EventActivityData {
    idactivity?: number;
    name: string;
    description?: string;
    state?: string;
    datelineDate?: Date;
    xp: number;
    event: EventData;
    users?: UserInEventActivityData[];
}

export interface BadgeToUserData {
    iduserbadge: number;
    unlockDate?: Date;
    targetCount: number;
    isCompleted?: boolean;
    badge: BadgeData;
    user: UserData;
}

export interface BadgeData {
    idbadge: number;
    name: string;
    description?: string;
    category: string;
    minLevel: number;
    cover?: string;
    targetCount: number;
    xp: number;
    unlockedBy: BadgeToUserData[];
}

export interface TrophyToGroupData {
    idgrouptrophy: number;
    unlockDate?: Date;
    isCompleted?: boolean;
    targetCount: number;
    trophy: TrophyData;
    group: GroupData;
}

export interface TrophyData {
    idtrophy: number;
    name: string;
    description?: string;
    category: string;
    type: string;
    targetCount: number;
    xp: number;
    awardedTo?: TrophyToGroupData[];
}

// Context

export interface UserContextType {
    user: UserData | undefined;
    setUser: (user: UserData | undefined) => void;
    isAdmin: boolean;
    setIsAdmin: (isAdmin : boolean) => void;
    activePage: string;
    handleChangePage: (activePage : string) => void;
    handleUserLogout: () => void;
}

export interface ThemeContextType {
    currentBackground: string;
    currentLogoBackground: string;
    currentStyle: string
    changeTheme: (level: number) => void;
    handleThemeLogout: () => void;
}

export interface ErrorContextType {
    messages: { id: number; text: string; isError: boolean }[];
    addMessage: (text: string, isError: boolean) => void;
    removeMessage: (id: number) => void;
    clearMessages: () => void;
}