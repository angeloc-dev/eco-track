import './AwardsContainer.css'
import {ReactElement, useEffect, useMemo, useState} from "react";
import Trophy from "./Trophy.tsx";
import Badge from "./Badge.tsx";
import {BadgeData, BadgeToUserData, TrophyData, TrophyToGroupData} from "../data/data-model.ts";
import {useError} from "../context/ErrorContext.tsx";
import {useUser} from "../context/UserContext.tsx";

interface AwardsFilterProps {
    toggle: string;
    filters: {
        radioOrder: string,
        radioFilter: string,
        category: string,
        type: string,
    }
    onFilterChange: (key: string, value: string) => void
}

function AwardsFilter({toggle, filters, onFilterChange}: AwardsFilterProps): ReactElement {
    const {isAdmin} = useUser();

    return (
        <div className="awards-filter container">
            <div className="flex-row">
                <label htmlFor="order-by" className="label-filter-awards">Order by:</label>
                <label className="events-radio">
                    <input type="radio" name={toggle === "Trophies" ? "trophyOrder" : "badgeOrder"} value="default"
                           checked={filters.radioOrder === "default"}
                           onChange={(e) => onFilterChange("radioOrder", e.target.value)}/>
                    <span className="radio-label"></span>
                    Default
                </label>
                <label className="events-radio">
                    <input type="radio" name={toggle === "Trophies" ? "trophyOrder" : "badgeOrder"} value="xp"
                           checked={filters.radioOrder === "xp"}
                           onChange={(e) => onFilterChange("radioOrder", e.target.value)}/>
                    <span className="radio-label"></span>
                    XP
                </label>
            </div>
            {toggle === "Trophies" && (
                <div className="flex-row">
                    <label htmlFor="trophies-category" className="label-filter-awards">Category</label>
                    <select className="category-select" value={filters.category}
                            onChange={(e) => onFilterChange("category", e.target.value)}>
                        <option value="">All Categories</option>
                        <option value="Family">Family</option>
                        <option value="School">School</option>
                        <option value="Business">Business</option>
                        <option value="EcoGroup">EcoGroup</option>
                    </select>
                    <label htmlFor="trophies-type">Type</label>
                    <select className="category-select" value={filters.type}
                            onChange={(e) => onFilterChange("type", e.target.value)}>
                        <option value="">All Types</option>
                        <option value="Common">Common</option>
                        <option value="Rare">Rare</option>
                        <option value="Legendary">Legendary</option>
                    </select>
                </div>
            )}
            {isAdmin && toggle === "Trophies" && (
                <div className="flex-row">
                    <label htmlFor="filter-by" className="label-filter-awards">Show:</label>
                    <label className="events-radio">
                        <input type="radio" name="trophyFilter" value="all"
                               checked={filters.radioFilter === "all"}
                               onChange={(e) => onFilterChange("radioFilter", e.target.value)}/>
                        <span className="radio-label"></span>
                        All {toggle}
                    </label>
                    <label className="events-radio">
                        <input type="radio" name="trophyFilter"
                               value="unlocked"
                               checked={filters.radioFilter === "unlocked"}
                               onChange={(e) => onFilterChange("radioFilter", e.target.value)}/>
                        <span className="radio-label"></span>
                        Unlocked {toggle}
                    </label>
                    <label className="events-radio">
                        <input type="radio" name="trophyFilter"
                               value="ongoing"
                               checked={filters.radioFilter === "ongoing"}
                               onChange={(e) => onFilterChange("radioFilter", e.target.value)}/>
                        <span className="radio-label"></span>
                        On Going {toggle}
                    </label>
                </div>
            )}
            {!isAdmin && toggle === "Badges" && (
                <div className="flex-row">
                    <label htmlFor="filter-by" className="label-filter-awards">Show:</label>
                    <label className="events-radio">
                        <input type="radio" name="badgeFilter" value="all"
                               checked={filters.radioFilter === "all"}
                               onChange={(e) => onFilterChange("radioFilter", e.target.value)}/>
                        <span className="radio-label"></span>
                        All {toggle}
                    </label>
                    <label className="events-radio">
                        <input type="radio" name="badgeFilter"
                               value="unlocked"
                               checked={filters.radioFilter === "unlocked"}
                               onChange={(e) => onFilterChange("radioFilter", e.target.value)}/>
                        <span className="radio-label"></span>
                        Unlocked {toggle}
                    </label>
                    <label className="events-radio">
                        <input type="radio" name="badgeFilter"
                               value="ongoing"
                               checked={filters.radioFilter === "ongoing"}
                               onChange={(e) => onFilterChange("radioFilter", e.target.value)}/>
                        <span className="radio-label"></span>
                        On Going {toggle}
                    </label>
                </div>
            )}
        </div>
    );
}

function AwardsContainer(): ReactElement {
    const [toggle, setToggle] = useState<string>("Trophies");
    const [trophies, setTrophies] = useState<TrophyData[]>([]);
    const [badges, setBadges] = useState<BadgeData[]>([]);
    const [trophiesProgress, setTrophiesProgress] = useState<TrophyToGroupData[]>([]);
    const [badgesProgress, setBadgesProgress] = useState<BadgeToUserData[]>([]);
    const {user, isAdmin} = useUser();
    const {messages, addMessage} = useError();
    const [filters, setFilters] = useState<{
        radioOrder: string;
        radioFilter: string;
        category: string;
        type: string;
    }>({
        radioOrder: "default", // Opzioni: 'default', 'xp'
        radioFilter: "all", // Opzioni: 'all' , 'unlocked'
        category: "",
        type: "",
    });

    useEffect(() => {
        if (!user) return;
        if (toggle === "Trophies") {
            fetch(`http://localhost:8080/trophies`, {credentials: "include"})
                .then((response) => {
                    if (!response.ok)
                        addMessage(`Error on request: ${response.status} - ${response.statusText}`, true);
                    return response.json();
                })
                .then((data: TrophyData[]) => setTrophies(data))
                .catch((err) => {
                    addMessage(err.message, true);
                });
            if (isAdmin) {
                fetch(`http://localhost:8080/users/${user?.iduser}/trophiesToGroupsAdmin`, { credentials: "include" })
                    .then((response) => {
                        if (!response.ok)
                            addMessage(`Error on request: ${response.status} - ${response.statusText}`, true);
                        return response.json();
                    })
                    .then((data: TrophyToGroupData[]) => setTrophiesProgress(data))
                    .catch((err) => {
                        addMessage(err.message, true);
                    });
            } else {
                fetch(`http://localhost:8080/users/${user?.iduser}/trophiesToGroups`, { credentials: "include" })
                    .then((response) => {
                        if (!response.ok)
                            addMessage(`Error on request: ${response.status} - ${response.statusText}`, true);
                        return response.json();
                    })
                    .then((data: TrophyToGroupData[]) => setTrophiesProgress(data))
                    .catch((err) => {
                        addMessage(err.message, true);
                    });
            }
        } else {
            fetch(`http://localhost:8080/badges`, { credentials: "include" })
                .then((response) => {
                    if (!response.ok)
                        addMessage(`Error on request: ${response.status} - ${response.statusText}`, true);
                    return response.json();
                })
                .then((data: BadgeData[]) => setBadges(data))
                .catch((err) => {
                    addMessage(err.message, true);
                });

            fetch(`http://localhost:8080/users/${user?.iduser}/badges`, { credentials: "include" })
                .then((response) => {
                    if (!response.ok)
                        addMessage(`Error on request: ${response.status} - ${response.statusText}`, true);
                    return response.json();
                })
                .then((data: BadgeToUserData[]) => setBadgesProgress(data))
                .catch((err) => {
                    addMessage(err.message, true);
                });
        }
    }, [user, toggle]);

    const filteredData: {
        content: TrophyData | BadgeData;
        progress: TrophyToGroupData | BadgeToUserData | undefined;
    }[] = useMemo(() => {
        // Selezioniamo direttamente l'array corretto in base a toggle
        const data = toggle === "Trophies" ? trophies : badges;
        const progressData = toggle === "Trophies" ? trophiesProgress : badgesProgress;

        let filtered = data.map((item) => ({
            content: item,
            progress: progressData.find((progress) => {
                if (toggle === "Trophies") {
                    return (progress as TrophyToGroupData).trophy?.idtrophy === (item as TrophyData).idtrophy;
                } else {
                    return (progress as BadgeToUserData).badge?.idbadge === (item as BadgeData).idbadge;
                }
            }),
        }));

        if (filters.radioFilter === "unlocked") {
            filtered = filtered.filter(({ progress }) => {
                if (!progress) return false;
                if (toggle === "Trophies") {
                    return (progress as TrophyToGroupData).isCompleted;
                } else {
                    return (progress as BadgeToUserData).isCompleted;
                }
            });
        }

        if (filters.radioFilter === "ongoing") {
            filtered = filtered.filter(({ progress }) => {
                if (!progress) return false;
                if (toggle === "Trophies") {
                    return !(progress as TrophyToGroupData).isCompleted;
                } else {
                    return !(progress as BadgeToUserData).isCompleted;
                }
            });
        }

        if (filters.category) {
            filtered = filtered.filter(({ content }) => content.category === filters.category);
        }

        if (filters.type && toggle === "Trophies") {
            filtered = filtered.filter(({ content }) => (content as TrophyData).type === filters.type);
        }

        if (filters.radioOrder === "xp") {
            filtered.sort((a, b) => b.content.xp - a.content.xp);
        }

        return filtered;
    }, [toggle, trophies, badges, trophiesProgress, badgesProgress, filters]);

    function handleFilterChange(key: string, value: string): void {
        setFilters((prev) => ({...prev, [key]: value}));
    }

    function handleToggle(): void {
        if (toggle === "Trophies") setToggle("Badges");
        else setToggle("Trophies");
    }

    return (
        <div className={messages.length > 0 ? "awards-container" : "awards-container awards-container-max"}>
            <div className={messages.length > 0 ? "awards-column" : "awards-column awards-column-max"}>
                <div className="flex-row">
                    <span className="title">{toggle}</span>
                    <span className="awards-empty"></span>
                    <span className="label-toggle-awards">Switch Trophies / Badges</span>
                    {toggle === "Trophies" ?
                        <img src="/icons/off-button.png" className="icon-toggle-awards" onClick={handleToggle}
                             alt="Toggle Off"/> :
                        <img src="/icons/on-button.png" className="icon-toggle-awards" onClick={handleToggle}
                             alt="Toggle On"/>
                    }
                </div>
                <AwardsFilter toggle={toggle} filters={filters} onFilterChange={handleFilterChange}/>
                <div className={messages.length > 0 ? "awards-list container" : "awards-list container awards-list-max"}>
                    {filteredData.map(({content, progress}) =>
                        toggle === "Trophies" ? (
                            <Trophy key={`trophy-${(content as TrophyData).idtrophy}`} content={content as TrophyData}
                                                         progressContent={progress as TrophyToGroupData | undefined}/>
                        ): (
                            <Badge key={`badge-${(content as BadgeData).idbadge}`} content={content as BadgeData}
                                   progressContent={progress as BadgeToUserData | undefined}/>
                    ))}
                </div>
            </div>
        </div>
    )
}

export default AwardsContainer;