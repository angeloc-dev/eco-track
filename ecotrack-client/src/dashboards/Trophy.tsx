import './Trophy.css'
import {useUser} from "../context/UserContext.tsx";
import {TrophyData, TrophyToGroupData} from "../data/data-model.ts";
import {ReactElement} from "react";

interface TrophyProps {
    content: TrophyData;
    progressContent: TrophyToGroupData | undefined;
}

function Trophy({content, progressContent}:TrophyProps): ReactElement | null {
    const {activePage} = useUser();

    if (!content) {
        return null;
    }

    if (activePage === "Events" && !progressContent) {
        return null;
    }

    const progressContentTargetCount = (): string => {
        if (progressContent === undefined) {
            return `0 / ${content.targetCount}`;
        } else if (progressContent.targetCount >= content.targetCount ) {
            return `Unlock on: ${progressContent.unlockDate}`;
        }  else {
            return `${progressContent.targetCount} / ${content.targetCount}`;
        }
    };

    const classLabelTargetCount = (): string => {
        if (progressContent === undefined) {
            return `awards-trophy-target-count`;
        } else if (progressContent.isCompleted && activePage === "Awards") {
            return `awards-trophy-target-count award-unlock-date`;
        } else {
            return `awards-trophy-target-count award-progress`;
        }
    };

    const classLabelName = (): string => {
        if (activePage === "Awards"){
            if (progressContent === undefined) return "trophy-name awards-trophy-name";
            else if (progressContent.targetCount >= content.targetCount ) return "awards-trophy-name awards-trophy-name-terminated";
            else return "trophy-name awards-trophy-name";
        } else {
            if (progressContent === undefined) return "trophy-name";
            else if (progressContent.targetCount >= content.targetCount ) return "awards-trophy-name-terminated";
            else return "trophy-name awards-trophy-name";
        }
    };

    const labelGroup = (): ReactElement | string => {
        if (activePage === "Events") return <></>;
        if ( progressContent) return ` - ${progressContent.group.name} `;
        else return <></>;
    };

    return (
        <div className="trophy">
            <div className="trophy-logo">
                <img className="trophy-logo-image"
                     src={`/trophies/trophies${content.type}.png`} alt={`Trophie ${content.type}`} />
            </div>
            <div className="trophy-content">
                <div className={activePage === "Awards" ? "flex-row-usergroup" : "flex-row"}>
                    <span>{content.type}</span>
                    {activePage === "Events" && (
                        <span>{progressContent?.group.name}</span>
                    )}
                    {activePage === "Awards" ?
                        <>
                            {activePage === "Awards" && (progressContent && progressContent?.targetCount >= content.targetCount) ? (
                                <span className="award-terminated">Terminated</span>
                            ) : (
                                <span className="awards-trophy-category">{content.category}</span>
                            )}
                        </> : null
                    }
                </div>
                <div className={activePage === "Awards" ? "flex-row-usergroup" : "flex-row"}>
                <span className={classLabelName()}>{content.name}{labelGroup()}</span>
                    {activePage === "Awards" ?
                        <span className="awards-trophy-xp">
                            <img className="activity-icon-xp" src="/icons/xp.png"
                                 alt="Xp Image"/>
                            {content.xp}
                        </span>
                        : null
                    }
                </div>
                <div className={activePage === "Awards" ? "flex-row-usergroup" : "flex-row"}>
                    {activePage === "Awards" ?
                        <span className="awards-trophy-description">{content.description}</span> : null
                    }
                    <span className={classLabelTargetCount()}>{progressContentTargetCount()}
                    </span>
                </div>
            </div>
        </div>
    )
}

export default Trophy;