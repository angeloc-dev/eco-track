import './Badge.css'
import {useUser} from "../context/UserContext.tsx";
import {BadgeData, BadgeToUserData} from "../data/data-model.ts";

interface BadgeProps {
    content: BadgeData;
    progressContent: BadgeToUserData | undefined;
}

function Badge({content, progressContent}: BadgeProps): JSX.Element | null {
    const {activePage, isAdmin} = useUser();

    if (!content) {
        return null;
    }
    if (activePage === "Challenges" && !progressContent) {
        return null;
    }

    const progressContentTargetCount = (): string => {
        if (!isAdmin) {
            if (progressContent === undefined) {
                return `0 / ${content.targetCount}`;
            } else if (progressContent.targetCount >= content.targetCount ) {
                return `${content.targetCount} / ${content.targetCount}`;
            }  else {
                return `${progressContent.targetCount} / ${content.targetCount}`;
            }
        }
        return ``;
    };

    const classLabelTargetCount = (): string => {
        if (!isAdmin) {
            if (progressContent === undefined) {
                return `badge-targetcount`;
            } else {
                return `badge-targetcount award-progress`;
            }
        }
        return ``;
    };

    const classLabelName = (): string => {
        if (progressContent === undefined || isAdmin) return "badge-name";
        else if (progressContent.targetCount >= content.targetCount ) return "badge-name badge-name-terminated";
        else return "badge-name";
    }

    return (
        <div className="badge">
            <div className="badge-logo">
                <img className={activePage === "Challenges" ? "badge-logo-image" : "awards-badge-logo-image"} src="/icons/approved.png" alt="Icon badge"/>
            </div>
            <div className="badge-content">
                <div className={activePage === "Challenges" ? "flex-row-expiry-awards" : "flex-row"}>
                    <span className={classLabelName()}>{content.name}</span>
                    {activePage === "Challenges" && (
                        <span className="badge-xp">
                            <img className="activity-icon-xp" src="/icons/xp.png" alt="Xp Image"/>
                            {content.xp}
                        </span>
                    )}
                    {activePage === "Awards" && (
                        <>
                            <span className="awards-empty"></span>
                            {progressContent?.targetCount === content.targetCount && !isAdmin? (
                                <span className="award-terminated"> Terminated
                            </span>
                            ) : (
                                <span className="awards-badge-min-level">
                                Min Level: {content.minLevel}
                            </span>
                            )}
                        </>
                    )}
                </div>
                <div className={activePage === "Challenges" ? "flex-row-expiry-awards" : "flex-row-usergroup"}>
                    {activePage === "Awards" && (
                        <div className="flex-row">
                            <span className="badge-description">{content.description}</span>
                        </div>
                    )}
                    {activePage === "Challenges" ?
                        <>
                            <span className="badge-description">{content.description}</span>
                            <span className={classLabelTargetCount()}> {progressContentTargetCount()}</span>
                        </>
                        :
                        <span className="awards-badge-xp">
                            <img className="activity-icon-xp" src="/icons/xp.png" alt="Xp Image"/>
                            {content.xp}
                        </span>}
                </div>
                <div className="flex-row flex-row-end">
                    {activePage === "Awards" && (
                        <>
                            <span className={classLabelTargetCount()}> {progressContentTargetCount()}</span>
                        </>
                    )}
                </div>
            </div>
        </div>);
}

export default Badge;